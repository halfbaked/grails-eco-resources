package com.saasplex.eco


import org.codehaus.groovy.grails.commons.ApplicationHolder


class EcoEngineTests extends grails.test.GrailsUnitTestCase {

  def ecoEngine

  void setUp(){
    def ecoJsPath = ApplicationHolder.application.parentContext.servletContext.getRealPath("/js/eco.js")
    def coffeeScriptJsPath = ApplicationHolder.application.parentContext.servletContext.getRealPath("/js/coffee-script-1.1.js")
    ecoEngine = new EcoEngine([ecoJsPath:ecoJsPath, coffeeScriptJsPath:coffeeScriptJsPath])  
  }

  void testCompile(){
    def templateName = 'person'
    def input = """
    <ul>
    <% for person in @people: %>
      <li><%= person.name %></li>      
    <% end %>
    </ul>
    """
    def output = ecoEngine.compile(templateName, input)
    assert output.contains('person.name') : "Unexpected output: $output"
  }
  
}
