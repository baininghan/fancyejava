package com.fancye.ibm.mq;

import java.awt.*;
import java.awt.event.*;
import javax.jms.*;
import javax.naming.*;

/**
 * A simple AWT GUI application demonstrating the basic capabilities 
 * of JMS publish/subscribe. A JMSPageSender publishes short text 
 * messages to a specified topic.
 */

public class JMSPageSender extends Frame
{

static InitialContext context;

   TextComponent topicText;
   TextComponent messageText;
   Button clearButton;
   Button sendButton;
   Label statusLabel;

   TopicConnection connection;
   TopicSession session;
   TopicPublisher publisher;

   int index = 0;

   /**
    * Runs the JMSPageSender application.
    */

   public static void main (String [] args)
   {
      try
      {
         context = new InitialContext (System.getProperties ());

         new JMSPageSender ().show ();
      }

      catch (JMSException jmse)
      {
         System.err.println ("JMS error: " + jmse + 
            " (" + jmse.getLinkedException () + ")");
         System.exit (0);
      }

      catch (NamingException ne)
      {
         System.err.println ("JNDI error: " + ne);
         System.exit (0);
      }
   }

   /**
    * Returns a named TopicConnection using the JNDI. The 
    * TopicConnectionFactory is a JMS-administered object.
    */

   static TopicConnection getConnection (String name) throws 
      JMSException, NamingException
   {
      TopicConnectionFactory factory = 
         (TopicConnectionFactory) context.lookup (name);

      return factory.createTopicConnection ();
   }

   /**
    * Returns a named Topic handle using the JNDI. The Topic 
    * is a JMS-administered object.
    */

   static Topic getTopic (String name) throws 
      JMSException, NamingException
   {
      return (Topic) context.lookup (name);
   }

   /**
    * Default constructor. Builds a simple AWT layout and then 
    * establishes a connection to the JMS service provider.
    */

   public JMSPageSender () throws 
      JMSException, NamingException
   {
      super ("JMSPageSender");

      Panel p;

      setLayout (new BorderLayout (6, 6));
      setBackground (Color.lightGray);
      add ("North", p = new Panel (new BorderLayout (6, 4)));
      p.add ("West", new Label ("Pager ID:"));
      p.add ("Center", topicText = new TextField ());
      p.add ("South", new Label ("Enter text message: "));
      add ("Center", messageText = new TextField (30));
      add ("South", p = new Panel (new BorderLayout (6, 4)));
      p.add ("West", clearButton = new Button (" Clear "));
      p.add ("East", sendButton = new Button (" Send "));
      p.add ("South", statusLabel = new Label ());
      pack ();
      setResizable (false);

      addWindowListener (new WindowAdapter () 
      {
         public void windowClosing (WindowEvent event)
         {
            disconnect ();
            System.exit (0);
         }
      });

      clearButton.addActionListener (new ActionListener ()
      {
         public void actionPerformed (ActionEvent event)
         {
            messageText.setText ("");
         }
      });

      sendButton.addActionListener (new ActionListener () 
      {
         public void actionPerformed (ActionEvent event)
         {
            publish (topicText.getText (), messageText.getText ());
         }
      });

      connection = getConnection(System.getProperty ("jmspager.server.name", "jmspager"));
      session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
      publisher = session.createPublisher (null);
   }

   /**
    * Publishes a text message to the named channel.
    */

   public void publish (String topicName, String messageText)
   {
      try
      {
         Topic topic = getTopic (topicName);
         TextMessage message = session.createTextMessage ();

         message.setText (messageText);
         publisher.publish (topic, message);
         showStatus (++index + ": published to " + topicName);
      }

      catch (JMSException jmse)
      {
         showError (jmse);
      }

      catch (NamingException ne)
      {
         showError (ne);
      }
   }

   /**
    * Drops the connection to the JMS service provider.
    */

   public void disconnect ()
   {
      try
      {
         publisher.close ();
         session.close ();
         connection.close ();
      }

      catch (JMSException jmse)
      {
         ;
      }
   }

   /**
    * Convenience method for displaying a message in the status area.
    */

   public void showStatus (String message)
   {
      statusLabel.setText (message);
   }

   /**
    * Convenience method for displaying an error.
    */

   public void showError (Exception e)
   {
      if (e instanceof JMSException)
      {
         System.err.println (e + 
            " (" + ((JMSException) e).getLinkedException () + ")");
      }
      else
      {
         System.err.println (e);
      }

      showStatus (e.getMessage ());
   }

   /**
    * Container method establishes a border margin (cosmetic).
    */

   public Insets getInsets ()
   {
      Insets insets = super.getInsets ();

      return new Insets (insets.top + 6, insets.left + 6, 
         insets.bottom, insets.right + 6);
   }
}


