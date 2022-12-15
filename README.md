# remotecrypto

The purpose of this project is to integrate these 3 services in one main server.

  ServerHTTP creates a HTTP connection that waits an url from a client with the desired text to hash.
  ServerTelnet creates a telnet socket that waits for a text from the client console to finally hash the message and return it.
  ServerFTP makes a ftp connection to hash all the files from the properties directory at the time set in properties.



