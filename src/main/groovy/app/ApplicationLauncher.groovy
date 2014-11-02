package app

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet

import static java.lang.System.getProperty

def port = getProperty("port", "8080")
def server = new Server(port as int)
server.handler = servletContextHandler()
server.start()
server.join()


def servletContextHandler() {
    def applicationContext = new AnnotationConfigWebApplicationContext()
    applicationContext.register(BootstrapConfiguration)

    def dispatcherServlet = new DispatcherServlet(applicationContext)
    def servletHolder = new ServletHolder(dispatcherServlet)
    def context = new ServletContextHandler()

    context.contextPath = "/"
    context.addServlet servletHolder, "/*"
    context
}


