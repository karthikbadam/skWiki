# skWiki

**skWiki** is a web application framework for collaborative creativity in digital multimedia projects, including text, hand-drawn sketches, and photographs. skWiki overcomes common drawbacks of existing wiki software by providing a rich viewer/editor architecture for all media types that is integrated into the web browser itself, thus avoiding dependence on client-side editors. Instead of files, skWiki uses the concept of paths as trajectories of persistent state over time. skWiki also provides a path viewer to visualize and navigate through the various paths/trajectories created during a collaborative design process. 

**How to use:**

skWiki has been built using Google Web Toolkit (GWT) (http://www.gwtproject.org/).  

The source code has two parts 1) the **src/** folder that contains the java source, 2) the **war/** folder with the web application source.

One can contribute to our project or use skWiki for their own purposes by creating an empty GWT project (http://www.gwtproject.org/doc/latest/tutorial/create.html) and copying the source code on this github page into the empty project. The dependencies can be found in **war/lib**. The compilation output of GWT is to be stored in the **war/** folder.

Alternatively, one can copy the contents of **war/** folder (web application), which contains the web application source, into a web server such as Tomcat (http://tomcat.apache.org/) and use skWiki right away. Note that this would require postgres server (the database schema can be found in schema.sql file).


Want to learn more? [See the wiki.](https://github.com/karthikbadam/skWiki/wiki)

For examples, [see the gallery](https://github.com/karthikbadam/skWiki/wiki/Gallery).


