When you add the jar files for the extension and its dependencies to the classpath in iReport, you may get the following exception:

javax.xml.bind.JAXBException: ClassCastException: attempting to cast jar somepath to differentpath

This is because the version of JAXB and JAXWS used by Apache Chemistry OpenCMIS is different than the version provided with Java.  It is essential that the dependencies shipped with the OpenCMIS client be used.  I experimented with the Java 6 version of these APIs, and OpenCMIS failed to correctly parse the repository descriptor, reporting that there were zero available repositories.

As a temporary workaround I placed the Apache OpenCMIS dependencies in my JVM /lib/endorsed directory.  This is far from ideal, but it will do until I sort out a better solution.