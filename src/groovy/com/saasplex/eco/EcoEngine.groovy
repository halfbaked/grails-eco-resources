package com.saasplex.eco


import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.shell.Global;
import org.springframework.core.io.ClassPathResource


// Eco engine uses Mozilla Rhino to compile the Eco template
// using existing javascript compilers
class EcoEngine {

  def Scriptable globalScope
  def ClassLoader classLoader
  def COMPILERS_DIR = 'js'

  def EcoEngine(){
    try {
      classLoader = getClass().getClassLoader()
      def ecoJsResource = (new ClassPathResource('com/saasplex/eco/eco.js', getClass().classLoader))
      assert ecoJsResource.exists() : "EcoJs resource not found at $opts.ecoJsPath"    
      // coffee-script-1.2 doesn't seem to compile in Rhino, so using coffee-script-1.1
      def coffeeScriptJsResource = (new ClassPathResource('com/saasplex/eco/coffee-script-1.1.js', getClass().classLoader))
      assert coffeeScriptJsResource.exists() : "CoffeeScriptJs resource not found"

      def ecoJsStream = ecoJsResource.inputStream
      def coffeeScriptJsStream = coffeeScriptJsResource.inputStream

      Context cx = Context.enter()
      cx.setOptimizationLevel(-1)
      globalScope = cx.initStandardObjects()
      cx.evaluateReader(globalScope, new InputStreamReader(coffeeScriptJsStream, 'UTF-8'), coffeeScriptJsResource.filename, 0, null)
      cx.evaluateReader(globalScope, new InputStreamReader(ecoJsStream, 'UTF-8'), ecoJsResource.filename, 1, null)    
    } catch (Exception e) {
      throw new Exception("Eco Engine initialization failed.", e)
    } finally {
      try {
        Context.exit()
      } catch (java.lang.IllegalStateException e) {}
    }
  }

  def compile(String templateName, String templateContent) {    
    try {
      def cx = Context.enter()
      def compileScope = cx.newObject(globalScope)
      compileScope.setParentScope(globalScope)
      compileScope.put("templateContent", compileScope, templateContent)
      def result = cx.evaluateString(compileScope, "eco.compile(templateContent, { identifier: '$templateName' })", "Eco compile command", 0, null)       
      return result
    } catch (Exception e) {
      throw new Exception("Eco Engine compilation failed.", e)
    } finally {
      Context.exit()
    }      
  }

}
