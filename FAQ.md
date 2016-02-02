# Frequently Asked Questions #

Here is the JMXetric FAQ

# Details #

  1. Does JMXetric work with the new wireformat introduced in Ganglia 3.1.x?
> > Yes it does.  You need to add "wireformat31x=true" to the agent config string passed into the JVM or add it in the config file.
  1. How do I enable JMXetric logging?
> > JMXetric uses the java logging framework to minimise dependencies on thirdparty libraries.  Add "-Djava.util.logging.config.file=myfile" to your JVM where myfile is your logging config file.  A simple sample is provided in the etc directory.