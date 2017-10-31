package edu.mst.grbcp5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CapitalizerServer extends Thread {

  private Socket socket;
  private int clientNumber;

  public CapitalizerServer( Socket socket, int clientNumber ) {
    this.socket = socket;
    this.clientNumber = clientNumber;

    log( "New connection with " + clientNumber + " at " + socket );
  }

  @Override
  public void run() {

    /* Local variables */
    BufferedReader in;
    PrintWriter out;
    ServerState serverState;
    String clientInput;
    CapitalizerProtocol request;
    boolean clientEngaged;

    out = null;

    try {

      in = new BufferedReader(
        new InputStreamReader(
          this.socket.getInputStream()
        )
      );
      out = new PrintWriter( socket.getOutputStream(), true );

      serverState = ServerState.WAITING_ON_REQUEST;
      clientEngaged = true;
      while( clientEngaged ) {

        /* Get new input */
        clientInput = in.readLine();

        switch( serverState ) {
          case WAITING_ON_REQUEST:

            /* Parse input as request */
            request = CapitalizerProtocol.getCapitalizerProtocol(
              new Integer( clientInput )
            );

            /* Respond to request */
            switch ( request ) {

              case CLIENT_REQUEST_CAPITALIZATION:
                out.println( CapitalizerProtocol.SERVER_ACCEPT.getIdentifier() );
                serverState = ServerState.RECIVING_STRING;
                break;
              case CLIENT_REQUEST_CLOSE:
                out.println( CapitalizerProtocol.SERVER_ACCEPT.getIdentifier() );
                clientEngaged = false;
                break;

              default:
                throw new Exception( "Could not parse client request" );

            } /* switch on request code */

            break;
          case RECIVING_STRING:

            out.println( clientInput.toUpperCase() );
            serverState = ServerState.WAITING_ON_REQUEST;

            break;

        } /* Switch on server state */

        out.println( clientInput.toUpperCase() );

      } /* Main process loop */

      socket.close();

    } catch ( Exception e ) {

      /* Try to tell client stuff messed up */
      try {
        out.println( CapitalizerProtocol.SERVER_REJECT.getIdentifier() );
      } catch ( Exception ignoreThisException ) {}

      log( "Error handing clint " + this.clientNumber + "'s request." );
      log( e.toString() );
    }

  }


  private void log( String msg ) {
    System.out.println( "Log: " + msg );
  }

}

enum ServerState {
  WAITING_ON_REQUEST,
  RECIVING_STRING
}
