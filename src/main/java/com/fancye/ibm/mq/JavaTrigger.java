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
/* Program name: JavaTrigger                                                 */
/*                                                                           */
/* Description: Sample java program triggered by WebSphere MQ                */
/*                                                                           */
/*****************************************************************************/
/*                                                                           */
/* Function:                                                                 */
/*                                                                           */
/*   This program shows how to implement a general java trigger application  */
/*   by retrieving dynamic information such as queue name and queue manager  */
/*   name that is passed to the application by the trigger monitor.          */
/*                                                                           */
/* This program must be launched by a WebSphere MQ trigger monitor.          */
/*                                                                           */
/*                                                                           */
/* Setup:                                                                    */
/*                                                                           */
/* Use runmqsc to create the following objects:                              */
/*   1. Queue to be triggered.                                               */
/*    DEFINE QLOCAL('JAVA.TRIGGER.QUEUE') REPLACE +                          */
/*           DESCR('Application queue to test triggering') +                 */
/*           SHARE +                                                         */
/*           TRIGGER +                                                       */
/*           TRIGTYPE (FIRST) +                                              */
/*           INITQ('JAVA.INIT.QUEUE') +                                      */
/*           PROCESS('JAVA.PROCESS')                                         */
/*                                                                           */
/*   2. Initiation queue.                                                    */
/*    DEFINE QLOCAL('JAVA.INIT.QUEUE') REPLACE +                             */
/*           DESCR('Initiation queue to test triggering')                    */
/*                                                                           */
/*   3. Process.   **                                                        */
/*    DEFINE PROCESS('JAVA.PROCESS')  REPLACE +                              */
/*           DESCR('Process to test triggering') +                           */
/*           APPLICID('java -cp .;c:\mqm\java\lib;c:\mqm\java\lib\com.ibm.mq.*/
/*jar;c:\mqm\java\lib\connector.jar;c:\mqm\java\lib\jta.jar JavaTrigger')    */
/*                                                                           */
/*   4. Start the trigger monitor with                                       */
/*      runmqtrm -m QMGRNAME -q JAVA.INIT.QUEUE                              */
/*   ** Note: It must be started in the directory where JavaTrigger.class    */
/*            resides so that JavaTrigger.class can be loaded from the       */
/*            current directory and resolved by "." in the classpath.        */
/*            If your java code is included in a package, then the jar may   */
/*            be added to the classpath and the location where runmqtrm is   */
/*            started does not matter.                                       */
/*                                                                           */
/*   5. Put a message on JAVA.TRIGGER.QUEUE. The trigger information along   */
/*      with the first 20 characters of the message is displayed in the      */
/*      window where runmqtrm is active.                                     */
/*                                                                           */
/*                                                                           */
/*   ** Note: The -cp paramater may be omitted if the classpath is already   */
/*            set in the environment.                                        */
/*            On UNIX, it may be preferred for the APPLICID to be the        */
/*            name of a shell script which sets up the environment and calls */
/*            the JVM. In this case, all parameters passed to the shell      */
/*            script must be passed on to the java application as a single   */
/*            parameter since the parameters include the MQTMC2 structure    */
/*            members. For example, in a kornshell script call the jvm with  */
/*              java JavaTrigger "$*"                                        */
/*                                                                           */
/*                                                                           */
/* This program has been tested with WebSphere MQ 5.3 and IBM JDK 1.3.1      */
/*                                                                           */
/*****************************************************************************/
import java.io.*;
import com.ibm.mq.*;

class MQTrigger
{

   private String structId;
   private String version;
   private String qName;
   private String processName;
   private String triggerData;
   private String applType;
   private String applId;
   private String envData;
   private String userData;
   private String qMgrName;

   /******************************************************/
   /* Constructor to parse the MQTMC2 stucture and set   */
   /* the class attributes.                              */
   /* Values derived from field definitions given for    */
   /* MQTMC2 in the WebSphere Application Programming    */
   /* Reference.                                         */
   /******************************************************/
   public MQTrigger(String tmcStruct) throws StringIndexOutOfBoundsException
   {

      structId    = tmcStruct.substring(0,3).trim();
      version     = tmcStruct.substring(4,8).trim();
      qName       = tmcStruct.substring(8,55).trim();
      processName = tmcStruct.substring(56,103).trim();
      triggerData = tmcStruct.substring(104,167).trim();
      applType    = tmcStruct.substring(168,171).trim();
      applId      = tmcStruct.substring(172,427).trim();
      envData     = tmcStruct.substring(428,555).trim();
      userData    = tmcStruct.substring(556,683).trim();
      qMgrName    = tmcStruct.substring(684,730).trim();

   }

   public String getStructId()
   {
      return(structId);
   }

   public String getVersion()
   {
      return(version);
   }

   public String getQueueName()
   {
      return(qName);
   }

   public String getProcessName()
   {
      return(processName);
   }

   public String getTriggerData()
   {
      return(triggerData);
   }

   public String getApplicationType()
   {
      return(applType);
   }

   public String getApplicationId()
   {
      return(applId);
   }

   public String getEnvironmentData()
   {
      return(envData);
   }

   public String getUserData()
   {
      return(userData);
   }

   public String getQueueManagerName()
   {
      return(qMgrName);
   }

};

public class JavaTrigger 
{


   private MQQueueManager qMgr;

   public static void main (String args[]) throws IOException 
   {
      if (args.length < 1)
      {
         System.out.println("This must be a triggered application");
      }
      else
      {
         JavaTrigger jt = new JavaTrigger();
         jt.start(args);
      }
      System.exit(0);
   }


   public void start(String args[]) 
   {
      try
      {

          MQException.log = null;

         /******************************************************/
         /* Create a MQTrigger class object to read the MQTMC2 */
         /* structure into the correct attribute.              */
         /******************************************************/
         MQTrigger tmc = new MQTrigger(args[0]);

         /******************************************************/
         /* Connect to the queue manager identified by the     */
         /* trigger.                                           */
         /******************************************************/

         qMgr = new MQQueueManager(tmc.getQueueManagerName());

         /******************************************************/
         /* Open the queue identified by the trigger.          */
         /******************************************************/

         int openOptions = MQC.MQOO_INPUT_AS_Q_DEF 
                         | MQC.MQOO_FAIL_IF_QUIESCING;

         MQQueue triggerQueue = qMgr.accessQueue(tmc.getQueueName(), 
                                                 openOptions,
                                                 null, null, null);

         /******************************************************/
         /* Set up our options to get the first message        */
         /* Wait 5 seconds to be cetain all messages are       */
         /* processed.                                         */
         /******************************************************/
         MQGetMessageOptions gmo = new MQGetMessageOptions();
         gmo.options = MQC.MQGMO_WAIT | MQC.MQGMO_CONVERT;
         gmo.waitInterval = 5000;

         MQMessage triggerMessage = new MQMessage();

         /*****************************************************/
         /* Read each message from the queue until there are  */
         /* no more messages to get.                          */
         /*****************************************************/
         long rc = 0;
         do
         {
            rc = 0;
            try
            {
               /***********************************************/
               /* Set the messageId and correlationId to none */
               /* to get all messages with no message         */
               /* selection.                                  */
               /***********************************************/
               triggerMessage.clearMessage();
               triggerMessage.correlationId = MQC.MQCI_NONE;
               triggerMessage.messageId = MQC.MQMI_NONE;

               triggerQueue.get(triggerMessage, gmo);
               String msg = triggerMessage.readString(triggerMessage.getMessageLength()); 

               /***********************************************/
               /* Insert business logic for the message here. */
               /* For this sample, echo the first 20          */
               /* characters of the message.                  */
               /***********************************************/
               if (msg.length() > 20)
               {
                  System.out.println("Message: " + msg.substring(0,20));
               }
               else
               {
                  System.out.println("Message: " + msg);
               }


            }
            catch (MQException mqEx)
            {
               rc = mqEx.reasonCode;
               if (rc != MQException.MQRC_NO_MSG_AVAILABLE)
               {
                  System.out.println(" PUT Message failed with rc = "  
                                     +  rc);
               }
            }
            catch (Exception ex)
            {
               System.out.println("Generic exception: " + ex);
               rc = 1;
            }

         } while (rc == 0);


         /**********************************************************/
         /* Cleanup MQ resources prior to exiting.                 */
         /**********************************************************/
         triggerQueue.close();
         qMgr.disconnect();
      }

      catch (MQException mqEx)
      {
         System.out.println("MQ failed with completion code = " 
                            + mqEx.completionCode 
                            + " and reason code = " + mqEx.reasonCode);
      }
   }

}


