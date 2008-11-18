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

package com.sun.mdm.index.eindex.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Logger;

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.DataObjectReader;
import com.sun.mdm.index.dataobject.InvalidRecordFormat;
import com.sun.mdm.index.dataobject.epath.DOEpath;
import com.sun.mdm.index.dataobject.objectdef.Lookup;
import com.sun.mdm.index.dataobject.objectdef.ObjectDefinition;
import com.sun.mdm.index.loader.config.LoaderConfig;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.epath.EPathParser;

/**
 * @author Sujit Biswas
 * 
 */
public class EIndexDataObjectReader implements DataObjectReader {

	private static Logger logger = Logger
			.getLogger(EIndexDataObjectReader.class.getName());

	private BufferedReader reader;

	private LoaderConfig config = LoaderConfig.getInstance();

	private ObjectDefinition objectDefinition;

	private Lookup personLookup;

	private Lookup addressLookup;

	private Lookup phoneLookup;

	private Lookup aliasLookup;

	/**
	 * 
	 */
	public EIndexDataObjectReader(String fileName) {
		try {
			reader = new BufferedReader(new FileReader(fileName));

			objectDefinition = config.getObjectDefinition();

			personLookup = Lookup.createLookup(objectDefinition);

			addressLookup = Lookup.createLookup(objectDefinition
					.getchild("Address"));

			phoneLookup = Lookup.createLookup(objectDefinition
					.getchild("Phone"));

			aliasLookup = Lookup.createLookup(objectDefinition
					.getchild("Alias"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private DataObject readRecord() {
		try {
			String record = reader.readLine();

			if (record == null) {
				return null;
			}

			Scanner s = new Scanner(record).useDelimiter("<>");

			String eventSegment = s.next();
			String idDemoAux = s.next();

			Scanner idDemo = new Scanner(idDemoAux).useDelimiter("&");

			DataObject d = createDataObject(idDemo.next(), idDemo.next());
			
			
			prcoessEventSegment(d, eventSegment);
			

			return d;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private void prcoessEventSegment(DataObject d, String eventSegment) {
		Scanner s = new Scanner(eventSegment).useDelimiter("\\|");
		
		
		String user_id = "";
		String update_date ="";
		for(int i=0; i< 8; i++){
			String str = getNextToken(s); //just read the first 8 tokens
			
			if(i==3){
				user_id = str;
			}
			
		}
		
		String date = getNextToken(s);
		String time = getNextToken(s);
		
		
		String date_time = date + " " + time;
		
		if(date.equals("")){
			date_time="";
		}
		
		try {
			update_date = formatDateTime(date_time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		d.add(2, user_id);
		d.add(2, update_date);
		
	}

	

	private DataObject createDataObject(String id, String demo) {
		// TODO add syscode lid and may be gid

		DataObject d = createDataObject(demo);

		Scanner s = new Scanner(id).useDelimiter("\\|");

		String segment_id = getNextToken(s);
		String eid_1 = getNextToken(s);
		String local_id = getNextToken(s);
		String non_unique_id = getNextToken(s);

		local_id = new Scanner(local_id).useDelimiter("~").next(); // take the
																	// first guy
																	// if more
																	// than one
																	// lid:syscode
																	// is
																	// present

		s = new Scanner(local_id).useDelimiter("\\^");

		String lid = getNextToken(s);
		String syscode = getNextToken(s);

		d.add(0, lid);
		d.add(0, syscode);

		return d;
	}

	private DataObject createDataObject(String demo) {
		DataObject d = new DataObject();

		try {
			Scanner s = new Scanner(demo).useDelimiter("\\|");

			String segment_id = s.next();

			// String person_category = s.next();
			setFieldValue(d, "Person.PersonCatCode", s.next(), personLookup);

			// String person_name = s.next();

			processPersonName(d, s.next());

			// String person_alias = s.next();

			processPersonAlias(d, s.next());

			// String alt_name = s.next();

			processAltName(d, s.next());

			processDOB(d, s);

			// String sex = s.next();

			setFieldValue(d, "Person.Gender", s.next(), personLookup);

			// String marital_status = s.next();

			setFieldValue(d, "Person.MStatus", s.next(), personLookup);

			// String SSN_number = s.next();

			setFieldValue(d, "Person.SSN", s.next(), personLookup);

			// String driver_license = s.next();

			//

			processDriverLicense(d, s.next());

			// String race = s.next();

			setFieldValue(d, "Person.Race", s.next(), personLookup);

			// String ethnic_group = s.next();

			setFieldValue(d, "Person.Ethnic", s.next(), personLookup);

			// String nationality = s.next();

			setFieldValue(d, "Person.Nationality", s.next(), personLookup);

			// String religion = s.next();

			setFieldValue(d, "Person.Religion", s.next(), personLookup);

			// String language = s.next();

			setFieldValue(d, "Person.Language", s.next(), personLookup);

			// String death = s.next();

			// setFieldValue(d, "Person.Death", s.next());

			processDeath(d, s.next());

			// String birth_place = s.next();

			processBirthPlace(d, s.next());

			// String vip = s.next();

			setFieldValue(d, "Person.VIPFlag", s.next(), personLookup);

			// String veteran_status = s.next();

			setFieldValue(d, "Person.VetStatus", s.next(), personLookup);

			// String military = s.next();

			processMilitary(d, s.next());

			// String citizenship = s.next();

			setFieldValue(d, "Person.Citizenship", s.next(), personLookup);

			String pension = s.next();
			String repatriation_number = s.next();
			String district_of_residence = s.next();
			String LGA_code = s.next();
			// String address = s.next();

			processAddress(d, s.next());
			// String phone = s.next();

			processPhone(d, s.next());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.info(e.getMessage());
		}

		// logger.info(d.toString());

		return d;
	}

	private void processDriverLicense(DataObject d, String driverLicense) {
		// TODO Auto-generated method stub
		// setFieldValue(d, "Person.DriverLicense", s.next());
		logger.info("DriverLicense : " + driverLicense);

		if (driverLicense.equals(""))
			return;

		try {
			Scanner s = new Scanner(driverLicense).useDelimiter("\\^");

			String state_country = getNextToken(s);
			setFieldValue(d, "Person.DriverLicenseSt", state_country,
					personLookup);

			String number = getNextToken(s);
			setFieldValue(d, "Person.DriverLicense", number, personLookup);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void processPhone(DataObject d, String next) {
		try {
			EPath e = EPathParser.parse("Person.Phone");

			logger.info("phone : " + next);

			Scanner s = new Scanner(next).useDelimiter("~");

			while (s.hasNext()) {
				String phone = s.next();

				DataObject child = new DataObject();
				DOEpath.addDataObject(e, d, child, personLookup);

				Scanner s1 = new Scanner(phone).useDelimiter("\\^");

				String type = getNextToken(s1);

				setFieldValue(child, "Phone.PhoneType", type, phoneLookup);

				String phone_number = getNextToken(s1);

				setFieldValue(child, "Phone.Phone", phone_number, phoneLookup);

				String phone_ext = getNextToken(s1);

				setFieldValue(child, "Phone.PhoneExt", phone_ext, phoneLookup);

				phone.toString();

			}
		} catch (Exception e) {
			logger.fine(e.getMessage());
			// e.printStackTrace();
		}

	}

	/**
	 * @param s
	 * @return
	 */
	private String getNextToken(Scanner s) {

		if (s.hasNext())
			return s.next();
		else
			return "";
	}

	private void processAddress(DataObject d, String next) {

		try {
			EPath e = EPathParser.parse("Person.Address");

			logger.info("address : " + next);

			Scanner s = new Scanner(next).useDelimiter("~");

			while (s.hasNext()) {
				String address = s.next();

				DataObject child = new DataObject();
				DOEpath.addDataObject(e, d, child, personLookup);

				Scanner addressFields = new Scanner(address)
						.useDelimiter("\\^");

				String type = getNextToken(addressFields);

				setFieldValue(child, "Address.AddressType", type, addressLookup);

				String street_1 = getNextToken(addressFields);

				setFieldValue(child, "Address.AddressLine1", street_1,
						addressLookup);

				String street_2 = getNextToken(addressFields);

				setFieldValue(child, "Address.AddressLine2", street_2,
						addressLookup);

				String street_3 = getNextToken(addressFields);
				String street_4 = getNextToken(addressFields);

				String city = getNextToken(addressFields);
				setFieldValue(child, "Address.City", city, addressLookup);

				String state = getNextToken(addressFields);
				setFieldValue(child, "Address.StateCode", state, addressLookup);

				String zip = getNextToken(addressFields);
				setFieldValue(child, "Address.PostalCode", zip, addressLookup);

				String zip_ext = getNextToken(addressFields);
				setFieldValue(child, "Address.PostalCodeExt", zip_ext,
						addressLookup);

				String county = getNextToken(addressFields);
				setFieldValue(child, "Address.County", county, addressLookup);

				String country = getNextToken(addressFields);
				setFieldValue(child, "Address.CountryCode", country,
						addressLookup);

				address.toString();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void processMilitary(DataObject d, String military) {
		logger.info("military: " + military);

		if (military.equals(""))
			return;

		try {
			Scanner s = new Scanner(military).useDelimiter("\\^");

			String status = getNextToken(s);
			// setFieldValue(d, "Person.PobCity", status, personLookup);

			String rank_grade = getNextToken(s);
			// setFieldValue(d, "Person.PobState", rank_grade, personLookup);

			String branch = getNextToken(s);
			// setFieldValue(d, "Person.PobCountry", branch, personLookup);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void processBirthPlace(DataObject d, String birthPlace) {
		logger.info("birthPlace: " + birthPlace);

		if (birthPlace.equals(""))
			return;

		try {
			Scanner s = new Scanner(birthPlace).useDelimiter("\\^");

			String city = getNextToken(s);
			setFieldValue(d, "Person.PobCity", city, personLookup);

			String state = getNextToken(s);
			setFieldValue(d, "Person.PobState", state, personLookup);

			String country = getNextToken(s);
			setFieldValue(d, "Person.PobCountry", country, personLookup);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void processDeath(DataObject d, String death) {
		logger.info("death: " + death);

		if (death.equals(""))
			return;

		try {
			Scanner s = new Scanner(death).useDelimiter("\\^");

			String flag = getNextToken(s);
			setFieldValue(d, "Person.Death", flag, personLookup);

			String death_of_death = getNextToken(s);

			setFieldValue(d, "Person.Dod", formatDate(death_of_death),
					personLookup);

			String death_certificate_number = getNextToken(s);
			setFieldValue(d, "Person.DeathCertificate",
					death_certificate_number, personLookup);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param d
	 * @param s
	 * @throws ParseException
	 * @throws EPathException
	 */
	private void processDOB(DataObject d, Scanner s) throws ParseException,
			EPathException {
		String date_of_birth = s.next();

		String time_of_birth = s.next();

		String dateStr = formatDate(date_of_birth);

		setFieldValue(d, "Person.DOB", dateStr, personLookup);
	}

	/**
	 * @param date_of_birth
	 * @return
	 * @throws ParseException
	 */
	private String formatDate(String date_of_birth) throws ParseException {

		if (date_of_birth.equals(""))
			return "";

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		Date date = df.parse(date_of_birth);

		df = new SimpleDateFormat(objectDefinition.getDateFormat());

		String dateStr = df.format(date);
		return dateStr;
	}
	
	
	/**
	 * @param date_of_birth
	 * @return
	 * @throws ParseException
	 */
	private String formatDateTime(String date_time) throws ParseException {

		if (date_time.equals(""))
			return "";

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		Date date = df.parse(date_time);

		df = new SimpleDateFormat(objectDefinition.getDateFormat() + " hh:mm:ss" );

		String dateStr = df.format(date);
		return dateStr;
	}

	private void processAltName(DataObject d, String alt_name) {
		logger.info("person_name: " + alt_name);

		if (alt_name.equals(""))
			return;

		try {
			Scanner s = new Scanner(alt_name).useDelimiter("\\^");

			String maiden_name = getNextToken(s);
			setFieldValue(d, "Person.Maiden", maiden_name, personLookup);

			String spouse_name = getNextToken(s);
			setFieldValue(d, "Person.SpouseName", spouse_name, personLookup);

			String mother_name = getNextToken(s);
			setFieldValue(d, "Person.MotherName", mother_name, personLookup);

			String fathers_name = getNextToken(s);
			setFieldValue(d, "Person.FatherName", fathers_name, personLookup);

			String mother_maiden_name = getNextToken(s);
			setFieldValue(d, "Person.MotherMN", mother_maiden_name,
					personLookup);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void processPersonAlias(DataObject d, String person_alias) {
		logger.info("person_alias: " + person_alias);

		if (person_alias.equals(""))
			return;

		try {

			EPath e = EPathParser.parse("Person.Alias");

			Scanner aliases = new Scanner(person_alias).useDelimiter("~");

			while (aliases.hasNext()) {
				String alias = aliases.next();

				DataObject child = new DataObject();
				DOEpath.addDataObject(e, d, child, personLookup);

				Scanner s = new Scanner(alias).useDelimiter("\\^");

				String last_name = getNextToken(s);
				setFieldValue(child, "Alias.LastName", last_name, aliasLookup);

				String first_name = getNextToken(s);
				setFieldValue(child, "Alias.FirstName", first_name, aliasLookup);

				String middle_name = getNextToken(s);
				setFieldValue(child, "Alias.MiddleName", middle_name,
						aliasLookup);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void processPersonName(DataObject d, String person_name) {
		logger.info("person_name: " + person_name);

		if (person_name.equals(""))
			return;

		try {
			Scanner s = new Scanner(person_name).useDelimiter("\\^");

			String last_name = getNextToken(s);
			setFieldValue(d, "Person.LastName", last_name, personLookup);

			String first_name = getNextToken(s);
			setFieldValue(d, "Person.FirstName", first_name, personLookup);

			String middle_name = getNextToken(s);
			setFieldValue(d, "Person.MiddleName", middle_name, personLookup);

			String title = getNextToken(s);
			setFieldValue(d, "Person.Title", title, personLookup);

			String suffix = getNextToken(s);
			setFieldValue(d, "Person.Suffix", suffix, personLookup);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setFieldValue(DataObject d, String epath, String value,
			Lookup lookup) throws EPathException {
		EPath e = EPathParser.parse(epath);

		DOEpath.setFieldValue(e, d, value, lookup);

		logger.info(epath + ":" + value);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EIndexDataObjectReader r = new EIndexDataObjectReader(
				"C:/test/loader/45loadproc.dat");

		try {
			while (true) {
				DataObject d = r.readDataObject();

				if (d == null) {
					break;
				}
			}
		} catch (InvalidRecordFormat e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see com.sun.mdm.index.dataobject.DataObjectReader#close();
	 */	
	public void close() throws Exception {
		reader.close();
	}

	/**
	 * @see com.sun.mdm.index.dataobject.DataObjectReader#reset();
	 */	
	public void reset() throws Exception {
		reader.reset();
	}
	
	public DataObject readDataObject() throws InvalidRecordFormat {
		return readRecord();
	}

}
