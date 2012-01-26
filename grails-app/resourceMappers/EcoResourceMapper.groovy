import org.grails.plugin.resource.mapper.*
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.codehaus.groovy.grails.commons.GrailsApplication
import grails.util.BuildSettings


class EcoResourceMapper implements GrailsApplicationAware {

  def pluginManager
  def buildSettings
  def phase = MapperPhase.GENERATION
  static defaultExcludes = ['**/*.js','**/*.png','**/*.gif','**/*.jpg','**/*.jpeg','**/*.gz','**/*.zip']
  GrailsApplication grailsApplication

  def map(resource, config) {
    File originalFile = resource.processedFile
    if (originalFile.name.endsWith('.eco')) {
      File input = grailsApplication.parentContext.getResource(resource.sourceUrl).file
      File target = new File("${originalFile.absolutePath}.js")         
      def templateName = "eco.$originalFile.name" - '.eco'
      if (log.debugEnabled) { log.debug "Compiling Eco file [$originalFile] into [$target]" }
      def ecoEngine = new com.saasplex.eco.EcoEngine()
      def output = """
        if(window.eco==undefined){ window.eco = {} }        
        ${ecoEngine.compile(templateName, input.text)}
      """
      target.write(output)
      resource.processedFile = target
      resource.updateActualUrlFromProcessedFile()
      resource.sourceUrlExtension = 'js'
      resource.contentType = 'text/javascript'
    }
  }

}
