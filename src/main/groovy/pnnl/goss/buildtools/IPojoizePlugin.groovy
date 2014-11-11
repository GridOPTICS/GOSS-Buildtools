package pnnl.goss.buildtools

import org.gradle.api.InvalidUserDataException
import org.gradle.api.Plugin;
import org.gradle.api.Project;


class IPojoizePlugin implements  Plugin<Project> {
	@Override
	void apply(Project project)  {
		
		def jar = project.tasks['jar']
		
		jar << {
			org.apache.felix.ipojo.manipulator.Pojoization pojo = new org.apache.felix.ipojo.manipulator.Pojoization()
			
			File jarfile = new File(jar.archivePath.toURI())
			File target = new File(jar.destinationDir.absolutePath + File.separator + jar.baseName + "_out.jar")
			
			File targetJarFile = new File(target.toURI())
			
			if (!jarfile.exists()) throw new InvalidUserDataException("The specified bundle file does not exist: " + jarfile.absolutePath)

			pojo.pojoization(jarfile, targetJarFile, (File) null)

			pojo.getWarnings().each { s ->
				println s
			}

			if (jarfile.delete()) {
				if ( !targetJarFile.renameTo(jarfile) ) {
					throw new InvalidUserDataException("Cannot rename the manipulated jar file");
				}
			}else {
				throw new InvalidUserDataException("Cannot delete the input jar file")
			}
		}
	}
}

