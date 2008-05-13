/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * The contents of this file are subject to the terms of the Common 
 * Development and Distribution License ("CDDL")(the "License"). You 
 * may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://open-dm-mi.dev.java.net/cddl.html
 * or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 * specific language governing permissions and limitations under the  
 * License.  
 *
 * When distributing the Covered Code, include this CDDL Header Notice 
 * in each file and include the License file at
 * open-dm-mi/bootstrap/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the 
 * fields enclosed by brackets [] replaced by your own identifying 
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 */
package com.sun.mdm.index.loader.matcher;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import com.sun.mdm.index.loader.config.LoaderConfig;
import com.sun.mdm.index.loader.common.LoaderException;

import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 * Merges many MatchFile (contains MatchRecord data) into one file.
 * Merge is done preserving the sorting order. So the output file
 * contains MatchRecord (GID1, GID2, weight) sorted from GID1, GID2.
 * Assumption: Each MatchFile is already sorted.
 * @author Swaranjit Dua
 *
 */
public class MatchFileMerger {

	private List<MatchRecord> curMatchList_ = new ArrayList<MatchRecord>();
	private List<MatchReader> readers_ = new ArrayList<MatchReader>();

	/*
	 * The above two List indices correspond to each other. So current MatchRecord from
	 * index "i" readers_ is stored in curMatchList_ at index "i".
	 */

	private MatchWriter writer_;
	private boolean isSBR_ = false;
	MatchFileMerger(boolean isSBR) {
		isSBR_ = isSBR;
	}

	MatchFileMerger() {

	}

	/**
	 * merge input files into one and write the output into outfile
	 * Merging is done by:
	 * 1. storing current value of MatchRecord from each file into a list.
	 * 2. compare MatchRecords in the list. Find the least records.
	 * 3. write the least record to the output file.
	 * 4. advance each such File whose current was least, by one record and store new
	 *    MatchRecord into the list.
	 * 5. If any file reaches its end, remove the file from the list.
	 * 6. Repeat 2-5 this until no more record from any file is left.
	 * 
	 * 
	 * Precondition: All MatchFiles contains MatchRecord in asceneding order.
	 * @param files list of files that have MatchRecord to be merged
	 * @param outfile contains merged data
	 * @throws LoaderException
	 */
	public void merge(List<File> files, File outfile) throws LoaderException {
		try{
			init(files, outfile);
			while(moreRecords()) {	   
				/*
				 * gets the MatchRecord positions in MatchList that are minimum 
				 * ;
				 */
				List<Integer> positions = leastRecords();

				MatchRecord least = curMatchList_.get(positions.get(0));
				writer_.write(least);
				List<Integer> removeList = new ArrayList<Integer>();

				/**
				 * get next MatchRecord from the relevant readers who had minimum
				 * current MatchRecord. All such readers need to retrieve next record
				 */

				for (int i = 0; i < positions.size(); i++) {
					int pos = positions.get(i);
					MatchRecord record = readers_.get(pos).next();
					if (record == null) {
						// so no more data in the reader and we'll remove them from the list after this loop
						removeList.add(pos);

					} else {
						// set the retrieved record to the recordList for that reader.
						setRecordtoList(record, pos);
					}
				}
				processRemoveList(removeList);
			}
			writer_.close();
		} catch (java.io.IOException io) {
			throw new LoaderException (io);
		}

	}


	private void init(List<File> files, File out) throws LoaderException {
		try {
			for (int i = 0; i < files.size(); i++) {
				MatchReader reader = null;
				if (!isSBR_) {
					reader = new MatchGIDRecordReader(files.get(i));
				} else {
					reader = new MatchEUIDRecordReader(files.get(i));
				}
				MatchRecord matchRecord = reader.next();
				if (matchRecord != null) {
					readers_.add(reader);
					curMatchList_.add(matchRecord);
				}

			}
			if (!isSBR_) {
				writer_ = new MatchGIDWriter(out);
			} else {
				writer_ = new MatchEUIDWriter(out);
			}
		} catch (java.io.FileNotFoundException fe) {
			throw new LoaderException (fe);
		}  catch (java.io.IOException e) {
			throw new LoaderException (e);
		}
	}


	private boolean moreRecords() {

		return (curMatchList_.size() >  0);
	}

	/*
	 * return the MatchRecord positions in MatchList that are minimum 
	 * ;
	 */
	private List<Integer> leastRecords() {

		/* stores list of all MatchList positions that are least.
		 * 
		 */
		List<Integer> least = new ArrayList<Integer>();
		least.add(0);

		for (int i = 1; i < curMatchList_.size(); i++) {
			int compare = curMatchList_.get(i).compare(curMatchList_.get(least.get(0)));
			if (compare < 0) {
				least = new ArrayList<Integer>(); // new minumum so resets list
				least.add(i);
			} else if (compare == 0) {
				least.add(i); // add one more to the list
			} 
			// do nothing for compare > 0

		}
		return least;
	}


	private void setRecordtoList(MatchRecord record, int recordNum) {
		curMatchList_.set(recordNum, record);
	}

	private void processRemoveList(List<Integer> removeList) {
		for (int i = 0; i < removeList.size(); i++) {
			int pos = removeList.get(i);
			int shiftedPos = pos - i; // use shifted position to remove element from the list.
			// becuase once an element is removed from list, the next position to be removed
			// is already shifted to the left by one. So the more elements are removed, the
			// more is shifted position to the left.
			curMatchList_.remove(shiftedPos);
			readers_.remove(shiftedPos);
		}
	}




	/*
	 *  this method is used for test purpose only. 
	 *  To change double to string to make it readable via editor.
	 */
	private static void changeMatchFileFormat(String dir) throws LoaderException {
		try {
			File fdir = new File(dir);

			String[] files = fdir.list();

			for (int i = 0; i < files.length; i++ ) {
				File file =  new File(dir, files[i]);
				String fileName = file.getName();
				String out = fileName + ".txt";
				File outfile = new File (dir, out);
				FileWriter fwriter = new FileWriter(outfile);

				BufferedWriter bwriter = new BufferedWriter(fwriter);

				MatchGIDRecordReader reader = new MatchGIDRecordReader(file);
				while(true) {

					MatchGIDRecord mr = (MatchGIDRecord) reader.next();
					if (mr == null) {
						break;
					}

					String gid1 = String.valueOf(mr.getGIDFrom());
					String gid2 = String.valueOf(mr.getGIDTo());
					String weight = String.valueOf(mr.getWeight());

					bwriter.write("," + gid1 + "," + gid2 + "," + weight);
					bwriter.newLine();

				}
				bwriter.close();

			}
		}catch (java.io.IOException e) {
			throw new LoaderException (e);
		}
	}


	private static void testMergeFiles(String dir) throws LoaderException {

		MatchFileMerger merger = new MatchFileMerger(true);
		File fdir = new File(dir);

		String[] files = fdir.list();
		File out = new File(dir,"MatchMerge");

		List<File> ffiles = new ArrayList<File>();

		for (int i = 0; i < files.length; i++ ) {
			File file =  new File(dir, files[i]);
			ffiles.add(file);
		}
		merger.merge(ffiles, out);		
	}

	public static void main(String[] args) {
		try {

			LoaderConfig config = LoaderConfig.getInstance();
			String workingDir = config.getSystemProperty("workingDir");

			String dir = workingDir + "\\match\\stage";    
			changeMatchFileFormat(dir);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());

		}

	}

}
