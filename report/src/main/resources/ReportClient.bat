@echo off
java -classpath lib\stc_eindex_client.jar;lib\j2ee.jar;lib\com.stc.rts.client.jar;lib\stc_eindex_report.jar;lib\ejb.jar;lib\ejb_service.jar;lib\stc_eindex_util.jar com.sun.mdm.index.report.ReportClient %1 %2 %3 %4
