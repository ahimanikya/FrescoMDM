/*
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
package com.sun.mdm.index.ejb.master.helper;

import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.objects.AddressObject;
import com.sun.mdm.index.objects.AliasObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.PhoneObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.objects.exception.ObjectException;

import java.util.Date;
import java.util.List;
import java.util.LinkedList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import java.text.ParseException;

import java.util.NoSuchElementException;


/** EiEvent 4.1.2 / 4.5 Parser
 * @author dcidon
 */
public class EiEventIterator implements SystemObjectIterator {
    private BufferedReader reader;
    private String currentLine;
    private TransactionObject trans;
    private SystemObject sysObj;
    private SystemObjectPK[] mAdditionalLids;
    private MasterController mMasterController;
    private String transFunction; //event_type_code
    private String transUserId; //user_id
    private String transSystem; //assigning_system
    private String transSource; //source
    private String transDept; //department
    private String transTerminalId; //terminal_id
    private String eventDate; //date_of_event
    private String eventTime;

    /** Creates new  EiEvent4_1_2_PersonIterator
     * @param masterController handle to master controller
     * @param newReader Reader object of one or more eiEvent messages
     * @throws ProcessingException error occured
     */
    public EiEventIterator(MasterController masterController, Reader newReader)
        throws ProcessingException {
        try {
            this.reader = new BufferedReader(newReader);
            getNextLine();
            mMasterController = masterController;
        } catch (IOException e) {
            throw new ProcessingException(e);
        }
    }
    
    // Get next non-comment line
    private void getNextLine() throws IOException {
        do {
            currentLine = reader.readLine();
        } while (currentLine != null && currentLine.startsWith("//"));
    }

    /** Get the next Person in the iterator
     * @return The next Person object in the iterator.
     * @throws ProcessingException error occured
     */
    public IteratorRecord next() throws ProcessingException {
        if (!hasNext()) {
            throw new NoSuchElementException("Reached end of iterator.");
        }
        SystemObject[] sysObjArray = null;
        try {
            parseMessage();
            sysObjArray = new SystemObject[1 + mAdditionalLids.length];
            sysObjArray[0] = sysObj;
            for (int i = 1; i < sysObjArray.length; i++) {
                SystemObject additionalSO = (SystemObject) sysObj.copy();
                additionalSO.setSystemCode(mAdditionalLids[i - 1].systemCode);
                additionalSO.setLID(mAdditionalLids[i - 1].lID);
                sysObjArray[i] = additionalSO;
            }
        } catch (Exception e) {
            throw new ProcessingException(e);
        } finally {
            //Get to the next record
            try {
                getNextLine();
            } catch (IOException e) {
                throw new ProcessingException(e);
            }
        }
        return new IteratorRecord(sysObjArray, trans);
    }

    private void parseMessage()
        throws ParseException, ObjectException, ProcessingException {
        //TODO Modify String Tokenizer to handle two character delimiter
        StringTokenizer st = new StringTokenizer(currentLine, '<');
        parseEventSegment(st.nextToken());
        parsePersonRecord(st.nextToken());
    }

    /**
     * @param token
     * @throws ParseException
     * @throws ObjectException
     * @throws ProcessingException  */
    private void parseEventSegment(String token)
        throws ParseException, ObjectException, ProcessingException {
        StringTokenizer st = new StringTokenizer(token, '|');
        st.nextToken(); //segment_ID
        st.nextToken(); //msg_ID
        transFunction = st.nextToken(); //event_type_code
        transUserId = st.nextToken(); //user_id
        transSystem = st.nextToken(); //assigning_system
        transSource = st.nextToken(); //source
        transDept = st.nextToken(); //department
        transTerminalId = st.nextToken(); //terminal_id
        eventDate = st.nextToken(); //date_of_event

        if (eventDate != null) {
            if (eventDate.length() > 8) {
                eventDate = StringUtil.stripChar(eventDate, '-');
            }

            //  is this correct?
            String eventTime0;

            if (st.hasMoreElements()) {
                eventTime0 = st.nextToken();

                if (eventTime0.length() > 6) {
                    eventTime = StringUtil.stripChar(eventTime0, ':');
                }
            } else {
                eventTime = "";
            }
        }
    }

    private void parsePersonRecord(String token)
        throws ParseException, ObjectException, ProcessingException {
        StringTokenizer st = new StringTokenizer(token, '&');
        String idSegment = st.nextToken();

        //Id Segment handling
        StringTokenizer st2 = new StringTokenizer(token, '|');
        st2.nextToken(); //segment_ID
        st2.nextToken(); //eid1

        String localIdNode = st2.nextToken(); //local_id

        StringTokenizer st3 = new StringTokenizer(localIdNode, '~');
        StringTokenizer st4 = new StringTokenizer(st3.nextToken(), '^');
        String lid = st4.nextToken(); //patient_ID
        String system = st4.nextToken(); //assigning_fac

        //TODO: Set transaction properties
        String childType = "Person";
        String status = "active";
        String createUser = "UI";
        String createFunction = "Add";
        Date createDateTime = new Date(System.currentTimeMillis());
        String updateUser = "UI";
        String updateFunction = "Add";
        Date updateDateTime = createDateTime;

        sysObj = new SystemObject(system, lid, childType, status,
                createUser, createFunction, createDateTime, updateUser,
                updateFunction, updateDateTime, new PersonObject());

        parseDemographicsSegment(st.nextToken());

        if (st.hasMoreElements()) {
            parseAuxSegment(st.nextToken());
        }
        
        //If there are other system objects, then clone existing one and
        //set system code and lid info.
        List l = new LinkedList();
        while (st3.hasMoreElements()) {
            st4 = new StringTokenizer(st3.nextToken(), '^');
            lid = st4.nextToken(); //patient_ID
            system = st4.nextToken(); //assigning_fac
            l.add(new SystemObjectPK(system, lid));
        }        
        mAdditionalLids = new SystemObjectPK[l.size()];
        for (int i = 0; i < mAdditionalLids.length; i++) {
            mAdditionalLids[i] = (SystemObjectPK) l.get(i);
        }        
    }

    private void parseDemographicsSegment(String token)
        throws ParseException, ObjectException, ProcessingException {
        PersonObject person = (PersonObject) sysObj.getObject();
        StringTokenizer st = new StringTokenizer(token, '|');
        st.nextToken(); //segment_id

        String nextToken = st.nextToken(); //person_category

        if (nextToken != null) {
            person.setPersonCatCode(nextToken);
        }

        parsePersonName(st.nextToken()); //person_name
        parsePersonAlias(st.nextToken()); //person_alias
        parseAlternateName(st.nextToken()); //alt_name
        nextToken = st.nextToken(); //date_of_birth

        if (token != null) {
            if (nextToken.length() > 8) {
                nextToken = StringUtil.stripChar(nextToken, '-');
            }

            EiDateFormat sdf = DateUtil.getDateFormat(EnumDateFormat.YYYYMMDD);
            person.setDOB(sdf.parse(nextToken));
            DateUtil.freeDateFormat(sdf);
        }

        st.nextToken(); //time_of_birth
        nextToken = st.nextToken(); //gender

        if (nextToken != null) {
            person.setGender(nextToken);
        }

        nextToken = st.nextToken(); //marital_status

        if (nextToken != null) {
            person.setMStatus(nextToken);
        }
        nextToken = st.nextToken(); //SSN_number

        /* if (nextToken != null) */ person.setSSN(nextToken);
        st.nextToken(); //driver_license
        nextToken = st.nextToken(); //race

        if (nextToken != null) {
            person.setRace(nextToken);
        }

        nextToken = st.nextToken(); //ethnic_group

        if (nextToken != null) {
            person.setEthnic(nextToken);
        }

        st.nextToken(); //nationality
        nextToken = st.nextToken(); //religon

        if (nextToken != null) {
            person.setReligion(nextToken);
        }

        nextToken = st.nextToken(); //language

        if (nextToken != null) {
            person.setLanguage(nextToken);
        }

        parseDeath(st.nextToken()); //death
        parseBirthPlace(st.nextToken()); //birth_place
        nextToken = st.nextToken(); //vip

        if (nextToken != null) {
            person.setVIPFlag(nextToken);
        }

        nextToken = st.nextToken(); //veteran_status

        if (nextToken != null) {
            person.setVetStatus(nextToken);
        }

        parseMilitary(st.nextToken()); //military
        nextToken = st.nextToken(); //citizenship

        if (nextToken != null) {
            person.setCitizenship(nextToken);
        }

        parsePension(st.nextToken()); //pension
        st.nextToken(); //repatriation_number

        if (nextToken != null) {
            person.setRepatriationNo(nextToken);
        }

        st.nextToken(); //district_of_residence

        if (nextToken != null) {
            person.setDistrictOfResidence(nextToken);
        }

        st.nextToken(); //LGA_code

        if (nextToken != null) {
            person.setLgaCode(nextToken);
        }

        String address = st.nextToken();

        if (address != null) {
            parseAddress(address); //address
        }

        if (st.hasMoreElements()) {
            String phone = st.nextToken();

            if (phone != null) {
                parsePhone(phone); //phone
            }
        }
    }

    private void parseMilitary(String token)
        throws ParseException, ObjectException, ProcessingException {
        PersonObject person = (PersonObject) sysObj.getObject();

        if (token != null) {
            StringTokenizer st = new StringTokenizer(token, '^');

            if (st.hasMoreElements()) {
                String nextToken = st.nextToken(); //status

                if (nextToken != null) {
                    person.setMilitaryStatus(nextToken);
                }

                if (st.hasMoreElements()) {
                    nextToken = st.nextToken(); //rank_grade

                    if (nextToken != null) {
                        person.setMilitaryRank(nextToken);
                    }

                    if (st.hasMoreElements()) {
                        nextToken = st.nextToken(); //branch

                        if (nextToken != null) {
                            person.setMilitaryBranch(nextToken);
                        }
                    }
                }
            }
        }
    }

    private void parsePension(String token)
        throws ParseException, ObjectException, ProcessingException {
        PersonObject person = (PersonObject) sysObj.getObject();

        if (token != null) {
            StringTokenizer st = new StringTokenizer(token, '^');

            if (st.hasMoreElements()) {
                String nextToken = st.nextToken(); //number

                if (nextToken != null) {
                    person.setPensionNo(nextToken);
                }

                if (st.hasMoreElements()) {
                    nextToken = st.nextToken(); //expiration_date

                    if (nextToken != null) {
                        if (nextToken.length() > 8) {
                            nextToken = StringUtil.stripChar(nextToken, '-');
                        }

                        EiDateFormat sdf = DateUtil.getDateFormat(EnumDateFormat.YYYYMMDD);
                        person.setPensionExpDate(new java.sql.Date(
                                sdf.parse(nextToken).getTime()));
                        DateUtil.freeDateFormat(sdf);
                    }
                }
            }
        }
    }

    private void parsePersonName(String token)
        throws ParseException, ObjectException, ProcessingException {
        PersonObject person = (PersonObject) sysObj.getObject();
        StringTokenizer st = new StringTokenizer(token, '^');
        String nextToken = st.nextToken(); //last_name

        if (nextToken != null) {
            person.setLastName(nextToken);
        }

        nextToken = st.nextToken(); //first_name

        if (nextToken != null) {
            person.setFirstName(nextToken);
        }

        if (st.hasMoreElements()) {
            nextToken = st.nextToken(); //middle_initial_or_name

            if ((nextToken != null) && !nextToken.equals("\"\"")) {
                person.setMiddleName(nextToken);
            }

            if (st.hasMoreElements()) {
                nextToken = st.nextToken(); //title

                if (nextToken != null) {
                    person.setTitle(nextToken);
                }

                if (st.hasMoreElements()) {
                    nextToken = st.nextToken(); //suffix

                    if (nextToken != null) {
                        person.setSuffix(nextToken);
                    }
                }
            }
        }
    }

    private void parseAlternateName(String token)
        throws ParseException, ObjectException, ProcessingException {
        PersonObject person = (PersonObject) sysObj.getObject();

        if (token != null) {
            StringTokenizer st = new StringTokenizer(token, '^');

            if (st.hasMoreElements()) {
                String nextToken = st.nextToken();

                if (nextToken != null) {
                    person.setMaiden(nextToken);
                }

                if (st.hasMoreElements()) {
                    nextToken = st.nextToken();

                    if (nextToken != null) {
                        person.setSpouseName(nextToken);
                    }

                    if (st.hasMoreElements()) {
                        nextToken = st.nextToken();

                        if (nextToken != null) {
                            person.setMotherName(nextToken);
                        }

                        if (st.hasMoreElements()) {
                            nextToken = st.nextToken();

                            if (nextToken != null) {
                                person.setFatherName(nextToken);
                            }

                            if (st.hasMoreElements()) {
                                nextToken = st.nextToken();

                                if (nextToken != null) {
                                    person.setMotherMN(nextToken);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void parsePersonAlias(String token)
        throws ParseException, ObjectException, ProcessingException {
        PersonObject person = (PersonObject) sysObj.getObject();

        if ((token != null) && !token.equals("")) {
            StringTokenizer st = new StringTokenizer(token, '~');

            while (st.hasMoreElements()) {
                StringTokenizer st2 = new StringTokenizer(st.nextToken(), '^');
                String firstName = st2.nextToken();
                String lastName = st2.nextToken();
                String middleName = "";

                if (st2.hasMoreElements()) {
                    middleName = st2.nextToken();
                }

                AliasObject alias = new AliasObject();
                alias.setFirstName(firstName);
                alias.setLastName(lastName);
                alias.setMiddleName(middleName);
                person.addAlias(alias);
            }
        }
    }

    private void parseDeath(String token)
        throws ParseException, ObjectException, ProcessingException {
        PersonObject person = (PersonObject) sysObj.getObject();

        if (token != null) {
            StringTokenizer st = new StringTokenizer(token, '^');

            if (st.hasMoreElements()) {
                String nextToken = st.nextToken(); //death_flag

                if (nextToken != null) {
                    person.setDeath(nextToken);
                }
            }
        }
    }

    private void parseBirthPlace(String token)
        throws ParseException, ObjectException, ProcessingException {
        PersonObject person = (PersonObject) sysObj.getObject();

        if (token != null) {
            StringTokenizer st = new StringTokenizer(token, '^');

            if (st.hasMoreElements()) {
                String nextToken = st.nextToken(); //place of birth, city

                if (nextToken != null) {
                    person.setPobCity(nextToken);
                }

                if (st.hasMoreElements()) {
                    nextToken = st.nextToken(); //place of birth, state

                    if (nextToken != null) {
                        person.setPobState(nextToken);
                    }

                    if (st.hasMoreElements()) {
                        nextToken = st.nextToken(); //place of birth, country

                        if (nextToken != null) {
                            person.setPobCountry(nextToken);
                        }
                    }
                }
            }
        }
    }

    private void parseAddress(String token)
        throws ParseException, ObjectException, ProcessingException {
        PersonObject person = (PersonObject) sysObj.getObject();
        StringTokenizer st2 = new StringTokenizer(token, '~');

        while (st2.hasMoreElements()) {
            StringTokenizer st = new StringTokenizer(st2.nextToken(), '^');
            String nextToken = st.nextToken(); //type

            if (nextToken != null) {
                AddressObject address = new AddressObject();
                address.setAddressType(nextToken);
                nextToken = st.nextToken(); //street_1

                if (nextToken != null) {
                    address.setAddressLine1(nextToken);
                }

                nextToken = st.nextToken(); //street_2

                if (nextToken != null) {
                    address.setAddressLine2(nextToken);
                }

                nextToken = st.nextToken(); //street_3

                if (nextToken != null) {
                    address.setAddressLine3(nextToken);
                }

                nextToken = st.nextToken(); //street_4

                if (nextToken != null) {
                    address.setAddressLine4(nextToken);
                }

                nextToken = st.nextToken(); //city

                if (nextToken != null) {
                    address.setCity(nextToken);
                }

                nextToken = st.nextToken(); //state

                if (nextToken != null) {
                    address.setStateCode(nextToken);
                }

                nextToken = st.nextToken(); //zip

                if (nextToken != null) {
                    address.setPostalCode(nextToken);
                }

                nextToken = st.nextToken(); //zip_ext

                if (nextToken != null) {
                    address.setPostalCodeExt(nextToken);
                }

                nextToken = st.nextToken(); //county

                if (nextToken != null) {
                    address.setCounty(nextToken);
                }

                nextToken = st.nextToken(); //country

                if (nextToken != null) {
                    address.setCountryCode(nextToken);
                }

                person.addAddress(address);
            }
        }
    }

    private void parsePhone(String token)
        throws ParseException, ObjectException, ProcessingException {
        PersonObject person = (PersonObject) sysObj.getObject();
        StringTokenizer st = new StringTokenizer(token, '~');

        while (st.hasMoreElements()) {
            StringTokenizer st2 = new StringTokenizer(st.nextToken(), '^');
            String phoneType = st2.nextToken();
            PhoneObject phone = new PhoneObject();
            phone.setPhoneType(phoneType);

            String phoneNumber = st2.nextToken();
            String phoneExt = st2.nextToken();
            phone.setPhone(phoneNumber);

            if (phoneExt != null) {
                phone.setPhoneExt(phoneExt);
            }

            person.addPhone(phone);
        }
    }

    private void parseAuxSegment(String token)
        throws ParseException, ObjectException, ProcessingException {
        StringTokenizer st = new StringTokenizer(token, '|');
        st.nextToken(); //segment_ID

        if (st.hasMoreElements()) {
            String classFields = st.nextToken();

            if (classFields != null) {
                parseClassFields(classFields);
            }

            if (st.hasMoreElements()) {
                String stringFields = st.nextToken();

                if (stringFields != null) {
                    parseStringFields(stringFields);
                }

                if (st.hasMoreElements()) {
                    String dateFields = st.nextToken();

                    if (dateFields != null) {
                        parseDateFields(dateFields);
                    }
                }
            }
        }
    }

    private void parseStringFields(String token)
        throws ParseException, ObjectException, ProcessingException {
        PersonObject person = (PersonObject) sysObj.getObject();
        StringTokenizer st = new StringTokenizer(token, '~');

        if (st.hasMoreElements()) {
            String nextToken = st.nextToken();

            if (nextToken != null) {
                person.setString1(nextToken);
            }

            if (st.hasMoreElements()) {
                nextToken = st.nextToken();

                if (nextToken != null) {
                    person.setString2(nextToken);
                }

                if (st.hasMoreElements()) {
                    nextToken = st.nextToken();

                    if (nextToken != null) {
                        person.setString3(nextToken);
                    }

                    if (st.hasMoreElements()) {
                        nextToken = st.nextToken();

                        if (nextToken != null) {
                            person.setString4(nextToken);
                        }

                        if (st.hasMoreElements()) {
                            nextToken = st.nextToken();

                            if (nextToken != null) {
                                person.setString5(nextToken);
                            }

                            if (st.hasMoreElements()) {
                                nextToken = st.nextToken();

                                if (nextToken != null) {
                                    person.setString6(nextToken);
                                }

                                if (st.hasMoreElements()) {
                                    nextToken = st.nextToken();

                                    if (nextToken != null) {
                                        person.setString7(nextToken);
                                    }

                                    if (st.hasMoreElements()) {
                                        nextToken = st.nextToken();

                                        if (nextToken != null) {
                                            person.setString8(nextToken);
                                        }

                                        if (st.hasMoreElements()) {
                                            nextToken = st.nextToken();

                                            if (nextToken != null) {
                                                person.setString9(nextToken);
                                            }

                                            if (st.hasMoreElements()) {
                                                nextToken = st.nextToken();

                                                if (nextToken != null) {
                                                    person.setString10(nextToken);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void parseDateFields(String token)
        throws ParseException, ObjectException, ProcessingException {
        PersonObject person = (PersonObject) sysObj.getObject();
        EiDateFormat sdf = DateUtil.getDateFormat(EnumDateFormat.YYYYMMDD);
        StringTokenizer st = new StringTokenizer(token, '~');

        if (st.hasMoreElements()) {
            String nextToken = st.nextToken();

            if (nextToken != null) {
                if (nextToken.length() > 8) {
                    nextToken = StringUtil.stripChar(nextToken, '-');
                    person.setDate1(new java.sql.Date(sdf.parse(nextToken).getTime()));
                }
            }

            if (st.hasMoreElements()) {
                nextToken = st.nextToken();

                if (nextToken != null) {
                    if (nextToken.length() > 8) {
                        nextToken = StringUtil.stripChar(nextToken, '-');
                        person.setDate2(new java.sql.Date(sdf.parse(nextToken).getTime()));
                    }
                }

                if (st.hasMoreElements()) {
                    nextToken = st.nextToken();

                    if (nextToken != null) {
                        if (nextToken.length() > 8) {
                            nextToken = StringUtil.stripChar(nextToken, '-');
                            person.setDate3(new java.sql.Date(sdf.parse(nextToken).getTime()));
                        }
                    }

                    if (st.hasMoreElements()) {
                        nextToken = st.nextToken();

                        if (nextToken != null) {
                            if (nextToken.length() > 8) {
                                nextToken = StringUtil.stripChar(nextToken, '-');
                                person.setDate4(new java.sql.Date(sdf.parse(nextToken).getTime()));
                            }
                        }

                        if (st.hasMoreElements()) {
                            nextToken = st.nextToken();

                            if (nextToken != null) {
                                if (nextToken.length() > 8) {
                                    nextToken = StringUtil.stripChar(nextToken, '-');
                                    person.setDate5(new java.sql.Date(sdf.parse(nextToken).getTime()));
                                }
                            }
                        }
                    }
                }
            }
        }

        DateUtil.freeDateFormat(sdf);
    }

    private void parseNonUniqueId(String token)
        throws ParseException, ObjectException, ProcessingException {
        PersonObject person = (PersonObject) sysObj.getObject();
        StringTokenizer st = new StringTokenizer(token, '~');

        while (st.hasMoreElements()) {
            StringTokenizer st2 = new StringTokenizer(st.nextToken(), '^');
            String id = st2.nextToken(); //id

            if (id != null) {
                if (st2.hasMoreElements()) {
                    String type = st2.nextToken(); //type
                } else {
                    throw new ParseException("Invalid Aux Id", 0);
                }
            }
        }
    }

    private void parseClassFields(String token)
        throws ParseException, ObjectException, ProcessingException {
        PersonObject person = (PersonObject) sysObj.getObject();
        StringTokenizer st = new StringTokenizer(token, '~');

        if (st.hasMoreElements()) {
            String nextToken = st.nextToken();

            if (nextToken != null) {
                person.setClass1(nextToken);
            }

            if (st.hasMoreElements()) {
                nextToken = st.nextToken();

                if (nextToken != null) {
                    person.setClass2(nextToken);
                }

                if (st.hasMoreElements()) {
                    nextToken = st.nextToken();

                    if (nextToken != null) {
                        person.setClass3(nextToken);
                    }

                    if (st.hasMoreElements()) {
                        nextToken = st.nextToken();

                        if (nextToken != null) {
                            person.setClass4(nextToken);
                        }

                        if (st.hasMoreElements()) {
                            nextToken = st.nextToken();

                            if (nextToken != null) {
                                person.setClass5(nextToken);
                            }
                        }
                    }
                }
            }
        }
    }

    /** Returns boolean indicating whether there are additional Person objects in
     * the iterator
     * @return Boolean indicating whether there are additional Person
     * objects in the iterator.
     */
    public boolean hasNext() {
        return (currentLine != null);
    }
}
