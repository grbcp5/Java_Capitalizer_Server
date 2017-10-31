package edu.mst.grbcp5;


public enum CapitalizerProtocol {
  INVALID_PROTOCOL( -1 ),

  SERVER_ACCEPT (202),
  SERVER_REJECT (406),

  CLIENT_REQUEST_CAPITALIZATION (0),
  CLIENT_REQUEST_CLOSE (1);

  private int identifier;

  CapitalizerProtocol( int ident ) {
    this.identifier = ident;
  }

  public int getIdentifier() {
    return identifier;
  }

  public static CapitalizerProtocol getCapitalizerProtocol( int ident ) {

    for ( CapitalizerProtocol protocol : CapitalizerProtocol.values() ) {
      if( ident == protocol.getIdentifier() ) {
        return protocol;
      }
    }

    return INVALID_PROTOCOL;
  }

}
