package com.mc.Mozo.config;
// !! jersey bug
// jersey-sprint3 includes SpringWebApplicationInitializer that initializes application context
// with new XmlWebApplicationContext(), reading configuration from applicationContext.xml by default.
// That means that we have to use applicationContext.xml for configuration.
// That also means that custom WebApplicationInitializer will compete with SpringWebApplicationInitializer
// for creating application context thus resulting in exceptions.

// There are possible workarounds, but without knowing how spring loads initializers internally,
// implementing them might result in nasty, hard to catch bugs

// Track jira bugs:
// https://java.net/jira/browse/JERSEY-2236
// https://java.net/jira/browse/JERSEY-2038

/*
 * package com.stp.config;
 * 
 * import org.springframework.web.context.AbstractContextLoaderInitializer; import
 * org.springframework.web.context.WebApplicationContext; import
 * org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
 * 
 * public class MHBWebApplicationInitializer extends AbstractContextLoaderInitializer {
 * 
 * @Override protected WebApplicationContext createRootApplicationContext() { AnnotationConfigWebApplicationContext
 * appContext = new AnnotationConfigWebApplicationContext(); appContext.register(ApplicationConfiguration.class); return
 * appContext; } }
 */
