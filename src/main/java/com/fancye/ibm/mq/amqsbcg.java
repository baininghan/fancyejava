package com.fancye.ibm.mq;

/*****************************************************************************/
/*                                                                           */
/* (c) Copyright IBM Corp. 2003  All rights reserved.                        */
/*                                                                           */
/* This sample program is owned by International Business Machines           */
/* Corporation or one of its subsidiaries ("IBM") and is copyrighted         */
/* and licensed, not sold.                                                   */
/*                                                                           */
/* You may copy, modify, and distribute this sample program in any           */
/* form without payment to IBM,  for any purpose including developing,       */
/* using, marketing or distributing programs that include or are             */
/* derivative works of the sample program.                                   */
/*                                                                           */
/* The sample program is provided to you on an "AS IS" basis, without        */
/* warranty of any kind.  IBM HEREBY  EXPRESSLY DISCLAIMS ALL WARRANTIES,    */
/* EITHER EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED     */
/* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.       */
/* Some jurisdictions do not allow for the exclusion or limitation of        */
/* implied warranties, so the above limitations or exclusions may not        */
/* apply to you.  IBM shall not be liable for any damages you suffer as      */
/* a result of using, modifying or distributing the sample program or        */
/* its derivatives.                                                          */
/*                                                                           */
/*****************************************************************************/
/*                                                                           */
/* Program name: amqsbcg                                                     */
/*                                                                           */
/* Description: Sample java program that browses a message queue             */
/*                                                                           */
/*****************************************************************************/
/*                                                                           */
/* Function:                                                                 */
/*                                                                           */
/*   This program is based on the amqsbcg0.c sample program.  It will browse */
/*   a queue and dump the message header and message body of messages that   */
/*   it finds on the queue.                                                  */
/*                                                                           */
/* This program is run as follows:                                           */
/*                                                                           */
/*     java amqsbcg -qm ... -q ...                                           */
/*                                                                           */
/* where                                                                     */
/*     -qm is the queue manager name                                         */
/*     -q  is the queue name                                                 */
/*                                                                           */
/* An alternate method is to run the program with an amqsbcg.properties file */
/* that contains the qm and q definitions:                                   */
/*                                                                           */
/*    qm=my.qmgr                                                             */
/*    q=SYSTEM.DEFAULT.LOCAL.QUEUE                                           */
/*                                                                           */
/* This program has been tested with MQSeries V5.2 CSD5/WebSphere MQ 5.3 and */
/* JDK 1.3.0.                                                                */
/*                                                                           */
/*****************************************************************************/
import java.io.*;
import java.util.Properties; 
/* import java.util.ResourceBundle; */ /* uncomment if using ResourceBundle */
                                       /* to get program's input            */
import java.util.Calendar;
import com.ibm.mq.*;

public class amqsbcg {

   private static String    myQmgr  = null;
   private static String    myQueue = null;

   /*******************************************************/
   /* Default constructor to read the properties file for */
   /* the qm and q parameters.                            */
   /*******************************************************/
   public amqsbcg() {

      /***********************************************/
      /* We'll get anything with the -D syntax first */
      /***********************************************/
      myQmgr  = System.getProperty("qm");
      myQueue = System.getProperty("q");

      /*************************************************/
      /* Now go and get the amqsbcg.properties file.   */
      /*************************************************/
      try {
         Properties props = new Properties(System.getProperties());
         props.load(new BufferedInputStream(new FileInputStream("amqsbcg.properties")));
         System.setProperties(props);
      } catch (Exception e) {
         System.out.println("Error getting amqsbcg.properties: " + e.getMessage());
         System.err.println(e);
      }
 
      /******************************************************/
      /* If any of the necessary properties are null, we'll */
      /* take the value from the file since they weren't    */
      /* specified with -D.                                 */
      /******************************************************/
      if (myQmgr  == null)       {myQmgr  = System.getProperty("qm");}
      if (myQueue == null)       {myQueue = System.getProperty("q");}

      /************************************************************/
      /* You could also use a ResourceBundle to get to the        */
      /* amqsbcg.properties file.                                 */
      /************************************************************/
      /* ResourceBundle rb = ResourceBundle.getBundle("amqsbcg"); */
      /* myQmgr  = rb.getString("qm");                            */
      /* myQueue = rb.getString("q");                             */
      /************************************************************/

   }

   /**********************************************************************/
   /* Constructor to read command line arguments (if they were supplied) */
   /**********************************************************************/
   public amqsbcg(String[] args) {

      this(); /* call the default constructor */

      /**********************************/ 
      /* Get the command-line arguments */ 
      /**********************************/ 
      for( int i=0; i<args.length; i++ ) {
          String arg = args[i].toLowerCase();

            if( arg.equals("-qm") ) {
                if ( i+1<args.length ) {
                   myQmgr = args[++i];
                } else {
                   System.out.println("didn't specify qmgr, exiting");
                   return;
                }

            } else if( arg.equals("-q") ) {
                if ( i+1<args.length ) {
                   myQueue = args[++i];
                } else {
                   System.out.println("didn't specify queue, exiting");
                   return;
                }

            } else {
              System.out.println( "Unknown argument: " + arg );
            }
      }

   }

   /********************************************************/
   /* This method does the actually browsing of the queue. */
   /********************************************************/
   public void myBrowser() {
      MQQueueManager qMgr        = null;
      MQQueue        browseQueue = null;
      int j = 0; /* used as a message counter */
      
      System.out.println("\namqsbcg.java - starts here");
      System.out.println("**************************");
      MQException.log = null; /* don't print out any exceptions */
      try {
         /*****************************/
         /* Create a queue manager    */
         /*****************************/
         qMgr = new MQQueueManager(myQmgr);

         /****************************************/
         /* Open the queue in browse mode.       */
         /****************************************/
         int openOptions = MQC.MQOO_BROWSE;
         browseQueue = qMgr.accessQueue(myQueue, openOptions, null, null, null);
         System.out.println("\n OPEN - '" + myQueue + "'\n\n");

         /*****************************************************************/
         /* Loop through the queue browsing the messages that are on it.  */
         /*****************************************************************/
         MQGetMessageOptions gmo = new MQGetMessageOptions(); 
         gmo.options = gmo.options + MQC.MQGMO_BROWSE_NEXT;
         MQMessage myMessage = new MQMessage();
         while (true) {
            myMessage.clearMessage();
            myMessage.correlationId = MQC.MQCI_NONE;
            myMessage.messageId     = MQC.MQMI_NONE;
            browseQueue.get(myMessage, gmo);
            j = j + 1;
            System.out.println(" GET of message number " + j);
            System.out.println("****Message descriptor****\n");
            System.out.println("  StrucId  : 'MD  '"    
                             + "  Version : " + myMessage.getVersion()); 
            System.out.println("  Report   : " + myMessage.report
                             + "  MsgType : " + myMessage.messageType);
            System.out.println("  Expiry   : " + myMessage.expiry
                             + "  Feedback : " + myMessage.feedback);
            System.out.println("  Encoding : " + myMessage.encoding 
                             + "  CodedCharSetId : " + myMessage.characterSet);
            System.out.println("  Format : '" + myMessage.format + "'");
            System.out.println("  Priority : " + myMessage.priority 
                             + "  Persistence : " + myMessage.persistence);
            System.out.print("  MsgId : ");
            dumpHexId(myMessage.messageId);
            System.out.print("  CorrelId : ");
            dumpHexId(myMessage.correlationId);
            System.out.println("  BackoutCount : " + myMessage.backoutCount);
            System.out.println("  ReplyToQ     : '" 
                             + myMessage.replyToQueueName + "'");
            System.out.println("  ReplyToQMgr  : '" 
                             + myMessage.replyToQueueManagerName + "'");

            System.out.println("  ** Identity Context");
            System.out.println("  UserIdentifier : '" + myMessage.userId + "'");
            System.out.println("  Accounting Token :");
            System.out.print("   ");
            dumpHexId(myMessage.accountingToken);
            System.out.println("  ApplIdentityData : '" 
                                + myMessage.applicationIdData + "'");
        
            System.out.println("  ** Origin Context");
            System.out.println("  PutApplType    : '" 
                               + myMessage.putApplicationType + "'");
            System.out.println("  PutApplName    : '" 
                               + myMessage.putApplicationName + "'");

            System.out.print("  PutDate  : '");
            System.out.print(myMessage.putDateTime.get(Calendar.YEAR));
            int myMonth = myMessage.putDateTime.get(Calendar.MONTH) + 1;
            if (myMonth < 10) {System.out.print("0");}
            System.out.print(myMonth);

            int myDay = myMessage.putDateTime.get(Calendar.DAY_OF_MONTH);
            if (myDay < 10) {System.out.print("0");}
            System.out.print(myDay);
            System.out.print("'    ");

            System.out.print("PutTime  : '");
            int myHour = myMessage.putDateTime.get(Calendar.HOUR_OF_DAY);
            if (myHour < 10) { System.out.print("0"); }
            System.out.print(myHour);

            int myMinute = myMessage.putDateTime.get(Calendar.MINUTE);
            if (myMinute < 10) { System.out.print("0"); }
            System.out.print(myMinute);

            int mySecond = myMessage.putDateTime.get(Calendar.SECOND);
            if (mySecond < 10) { System.out.print("0"); }
            System.out.print(mySecond);

            int myMsec = myMessage.putDateTime.get(Calendar.MILLISECOND);
            myMsec = myMsec/10;
            if (myMsec < 10) { System.out.print("0"); }
            System.out.print(myMsec); 
            System.out.println("'");

            System.out.println("  ApplOriginData : '" 
                               + myMessage.applicationOriginData + "'");
            System.out.println();
            System.out.print("  GroupId : ");
            dumpHexId(myMessage.groupId);
            System.out.println("  MsgSeqNumber   : '" 
                               + myMessage.messageSequenceNumber + "'");
            System.out.println("  Offset         : '" + myMessage.offset + "'");
            System.out.println("  MsgFlags       : '" 
                               + myMessage.messageFlags + "'");
            System.out.println("  OriginalLength : '" 
                               + myMessage.originalLength + "'");
            System.out.println();

            System.out.println("****   Message     ****");
            System.out.println();
            System.out.println(" length - " + myMessage.getMessageLength() 
                             + " bytes\n");
            dumpHexMessage(myMessage);
            System.out.println();
            System.out.println();

         }

      } catch (MQException ex) {
         if (ex.reasonCode == MQException.MQRC_NO_MSG_AVAILABLE) {
            System.out.println(" No more messages");
         } else {
            System.out.println("MQ error: Completion code " +
                      ex.completionCode + " Reason code " + ex.reasonCode);
         }
      } catch (java.io.IOException ex) {
         System.out.println("An IO error occurred: " + ex);
      }
        
      try {
         browseQueue.close();
         System.out.println(" CLOSE of queue");
         qMgr.disconnect();
         System.out.println(" DISCONNECT from queue manager");
      } catch (MQException ex) {
         System.out.println("MQ error: Completion code " +
                      ex.completionCode + " Reason code " + ex.reasonCode);
      }

      System.out.println("**************************");
      System.out.println("amqsbcg.java finished");

   } 

   /*************************************************************/
   /* This method will dump the text of the message in both hex */
   /* and character format.                                     */
   /*************************************************************/
   static void dumpHexMessage(MQMessage myMsg) throws java.io.IOException {
       
      int DataLength = myMsg.getMessageLength();
      int ch      = 0;
      int chars_this_line = 0;
      int CHARS_PER_LINE  = 16;
      StringBuffer line_text = new StringBuffer();
      line_text.setLength(0);
      do {
         chars_this_line = 0;
         String myPos = new String(Integer.toHexString(ch));
         for (int i=0; i < 8 - myPos.length(); i++) {
            System.out.print("0");
         }
         System.out.print((String)(Integer.toHexString(ch)).toUpperCase() + ": ");

         while ( (chars_this_line < CHARS_PER_LINE) &&
                 (ch < DataLength) ) {

             if (chars_this_line % 2 == 0) {
                System.out.print(" ");
             }
             char b = (char)(myMsg.readUnsignedByte() & 0xFF);
             if (b < 0x10) {
                System.out.print("0");
             }

             System.out.print((String)(Integer.toHexString(b)).toUpperCase());

             /***********************************************/ 
             /* If this is a printable character, print it. */
             /* Otherwise, print a '.' as a placeholder.    */
             /***********************************************/ 
             if (Character.isLetterOrDigit(b)
                 || Character.getType(b) == Character.CONNECTOR_PUNCTUATION
                 || Character.getType(b) == Character.CURRENCY_SYMBOL
                 || Character.getType(b) == Character.MATH_SYMBOL
                 || Character.getType(b) == Character.MODIFIER_SYMBOL
                 || Character.getType(b) == Character.UPPERCASE_LETTER
                 || Character.getType(b) == Character.SPACE_SEPARATOR
                 || Character.getType(b) == Character.DASH_PUNCTUATION
                 || Character.getType(b) == Character.START_PUNCTUATION
                 || Character.getType(b) == Character.END_PUNCTUATION
                 || Character.getType(b) == Character.OTHER_PUNCTUATION ) {
                line_text.append(b); 
             } else {
                line_text.append('.');
             }
             chars_this_line++;
             ch++;
         }

         /*****************************************************/
         /* pad with blanks to format the last line correctly */
         /*****************************************************/
         if (chars_this_line < CHARS_PER_LINE) {
           for ( ;chars_this_line < CHARS_PER_LINE; chars_this_line++) {
              if (chars_this_line % 2 == 0) System.out.print(" ");
              System.out.print("  ");
              line_text.append(' ');
           }
         }

         System.out.println(" '" + line_text.toString() + "'");
         line_text.setLength(0);
       } while (ch < DataLength);

   } /* end of dumpHexMessage */

   /****************************************************/
   /* Some of the MQ Ids are actually byte strings and */
   /* need to be dumped in hex format.                 */
   /****************************************************/
   static void dumpHexId(byte[] myId) {
      System.out.print("X'");
      for (int i=0; i < myId.length; i++) {
        char b = (char)(myId[i] & 0xFF);
        if (b < 0x10) {
           System.out.print("0");
        }
        System.out.print((String)(Integer.toHexString(b)).toUpperCase());
      }
      System.out.println("'");
   }


    /****************************/
    /* main program entry point */
    /****************************/
    public static void main( String[] args )     {
       amqsbcg app = new amqsbcg(args);
       
       /******************************************/ 
       /* Check that all arguments were entered. */ 
       /******************************************/ 
       if (     (myQmgr==null)  
             || (myQueue==null) ) {
          System.out.println("Usage:");
          System.out.println("java amqsbcg -qm ... -q ...");
          System.out.println("where -qm is the queue manager name");
          System.out.println("      -q  is the queue name");
       } else {
          app.myBrowser();
       }
    }

   
}


