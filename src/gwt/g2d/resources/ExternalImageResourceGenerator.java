/*
 * Copyright 2009 Hao Nguyen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package gwt.g2d.resources;

import gwt.g2d.resources.client.AbstractImageElementResource;
import gwt.g2d.resources.client.ExternalImageResource;
import gwt.g2d.resources.client.ImageElementResource;
import gwt.g2d.resources.client.ImageLoader;

import java.net.URL;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.resources.client.ResourceCallback;
import com.google.gwt.resources.ext.AbstractResourceGenerator;
import com.google.gwt.resources.ext.ResourceContext;
import com.google.gwt.resources.ext.ResourceGeneratorUtil;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.user.rebind.StringSourceWriter;

/**
 * Provides the generator for {@link ExternalImageResource}
 * 
 * @author hao1300@gmail.com
 */
public final class ExternalImageResourceGenerator extends
		AbstractResourceGenerator {
	
	@Override
  public String createAssignment(TreeLogger logger, ResourceContext context,
      JMethod method) throws UnableToCompleteException {
    String name = "ExternalImageResourceGenerator.createAssignment";
		URL[] resources = ResourceGeneratorUtil.findResources(logger, context,
        method);
		
    SourceWriter sw = new StringSourceWriter();
    // Write the expression to create the subtype.
    sw.println("new " + ExternalImageResource.class.getName() + "() {");
    sw.indent();
    
    sw.println(String.format("public void getImage(%s<%s> callback) {", 
    		ResourceCallback.class.getName(),
    		ImageElementResource.class.getName()));
    sw.indent();
    
    int index = 0;
    String abstractImageElementResourceClassName = 
    		AbstractImageElementResource.class.getName();
    for (URL resource : resources) {
			String outputUrlExpression = context.deploy(resource, false);
			sw.println("{");
			sw.println(abstractImageElementResourceClassName + " imgResource = new " 
					+ abstractImageElementResourceClassName + "() {");
			
			sw.indent();
			
			sw.println("public String getName() {");
			sw.indent();
			sw.println("return \"" + name + "\";");
			sw.outdent();
			sw.println("}");
			
			sw.println("public String getBaseUrl() {");
			sw.indent();
			sw.println("return \"" + ResourceGeneratorUtil.baseName(resource) + "\";");
			sw.outdent();
			sw.println("}");
			
			sw.println("public int getIndex() {");
			sw.indent();
			sw.println("return " + index + ";");
			sw.outdent();
			sw.println("}");
			
			sw.outdent();
			sw.println("};");
			
	    sw.println(ImageLoader.class.getName() + ".loadImageAsync("
	    		+ outputUrlExpression + ", imgResource, callback);");
	    sw.println("}");
	    index++;
    }
    sw.outdent();
    sw.println("}");
    
    sw.println("public String getName() {");
    sw.indent();
    sw.println("return \"" + name + "\";");
    sw.outdent();
    sw.println("}");

    sw.outdent();
    sw.println("}");

    return sw.toString();
	}
}
