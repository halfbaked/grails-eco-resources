This is a Grails plugin than enables the easy inclusion of eco templates into a Grails appplication. 
It requires the well established resources plugin.

## Background

[Eco](https://github.com/sstephenson/eco) is yet another Javascript templating tool. Eco is written by PrototypeJs 
author [Sam Stephenson](https://github.com/sstephenson). 

It uses Mozilla's [Rhino engine](https://github.com/matthieu/rhymeno) to execute the original eco compiler written in javascript.

## Usage

To add eco template files to your grails project:

* Install the plugin ( I have yet to add it to the Plugin Portal )
* Actually add the eco tempalte files to your project. I placed mine adjacent to the js folder in eco.
* Reference your eco files in your ApplicationResources file (or where ever your defining your resources)

Example
<pre>
  eco {
    resource url: 'eco/dir.eco'
    resource url: 'eco/entry.eco'
    resource url: 'eco/category.eco'
  }  
</pre>

The above example will create a resource you can include in pages or have another resource depend on. The templates 
are converted into javascript objects in the Global name space. The naming of these js objects follows the pattern "eco.$fileName"
where the filename does not include the extension.

Example eco tempate people.eco

<pre>
  &lt;ul&gt;
    &lt;% for person in @people: %&gt;
      &lt;li&gt; &lt;%= person.name %&gt;&lt;/li&gt;      
    &lt;% end %&gt;
  &lt;/ul&gt;
</pre>

which would be referenced in your javascript code as:
<pre>
  $("li").html( eco.people( { people: [{ name: 'John' }, { name: 'Mary'}] } ) );
</pre>