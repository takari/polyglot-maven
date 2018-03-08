public class pom1 extends org.sonatype.maven.polyglot.java.dsl.ModelFactory {
	
    public void project() {
    	
        modelVersion = "4.0.0";
        groupId = "null";
        artifactId = "maven";
        packaging = "pom";
        version = "3.3.7-SNAPSHOT";
        
        parent(
            groupId -> "org.apache.maven",
            artifactId -> "maven-parent",
            version -> "26",
            relativePath -> "../pom/maven/pom.xml"
        );
        
        properties(
            property("checkstyle.violation.ignore", "RedundantThrows,NewlineAtEndOfFile,ParameterNumber,MethodLength,FileLength,JavadocType,MagicNumber,InnerAssignment,MethodName"),
            property("checkstyle.excludes", "**/package-info.java"),
            property("aetherVersion", "1.0.2.v20150114"),
            property("guavaVersion", "18.0"),
            property("junitVersion", "4.11"),
            property("plexusUtilsVersion", "3.0.22"),
            property("classWorldsVersion", "2.5.2"),
            property("guiceVersion", "4.0"),
            property("sisuInjectVersion", "0.3.2"),
            property("maven.compiler.source", "1.7"),
            property("modelloVersion", "1.8.3"),
            property("distributionName", "Apache Maven"),
            property("plexusVersion", "1.6"),
            property("jxpathVersion", "1.3"),
            property("maven.test.redirectTestOutputToFile", "true"),
            property("commonsCliVersion", "1.2"),
            property("maven.compiler.target", "1.7"),
            property("plexusInterpolationVersion", "1.21"),
            property("distributionShortName", "Maven"),
            property("wagonVersion", "2.9"),
            property("maven.site.path", "ref/3-LATEST"),
            property("securityDispatcherVersion", "1.3"),
            property("distributionId", "apache-maven"),
            property("slf4jVersion", "1.7.5"),
            property("cipherVersion", "1.7")
        );
        
        dependencyManagement(
            dependency(
                groupId -> "junit",
                artifactId -> "junit",
                version -> "${junitVersion}",
                scope -> "test",
                type -> "jar"
            )
        );
        
        dependencies(
            dependency(
                groupId -> "junit",
                artifactId -> "junit",
                version -> "${junitVersion}",
                scope -> "test",
                type -> "jar"
            )
        );
        
        build()
            .pluginManagement(
                plugin("org.codehaus.plexus:plexus-component-metadata:${plexusVersion}")
                .endPlugin()
                ,
                plugin("org.eclipse.sisu:sisu-maven-plugin:${sisuInjectVersion}")
                .endPlugin()
                ,
                plugin("org.apache.maven.plugins:maven-release-plugin")
                    .configuration(
                        startXML()
                        .tag("configuration", configuration -> {
                            configuration.child("autoVersionSubmodules",autoVersionSubmodules -> {
                                autoVersionSubmodules.content("true");
                            });
                        })
                        .endXML()
                    ) // end of configuration section
                .endPlugin()
                ,
                plugin("org.apache.maven.plugins:maven-surefire-plugin")
                    .configuration(
                        startXML()
                        .tag("configuration", configuration -> {
                            configuration.child("argLine",argLine -> {
                                argLine.content("-Xmx256m");
                            });
                        })
                        .endXML()
                    ) // end of configuration section
                .endPlugin()
                ,
                plugin("org.codehaus.modello:modello-maven-plugin:${modelloVersion}")
                .endPlugin()
                ,
                plugin("org.apache.felix:maven-bundle-plugin:1.0.0")
                .endPlugin()
                ,
                plugin("org.codehaus.mojo:buildnumber-maven-plugin:1.2")
                .endPlugin()
                ,
                plugin("org.apache.maven.plugins:maven-site-plugin")
                    .configuration(
                        startXML()
                        .tag("configuration", configuration -> {
                            configuration.child("topSiteURL",topSiteURL -> {
                                topSiteURL.content("scm:svn:https://svn.apache.org/repos/infra/websites/production/maven/components/${maven.site.path}");
                            });
                        })
                        .endXML()
                    ) // end of configuration section
                .endPlugin()
                ,
                plugin("org.apache.maven.plugins:maven-scm-publish-plugin:1.1")
                .endPlugin()
                ,
                plugin("org.apache.rat:apache-rat-plugin")
                    .configuration(
                        startXML()
                        .tag("configuration", configuration -> {
                            configuration.child("excludes",excludes -> {
                                excludes.child("exclude",exclude -> {
                                    exclude.content("src/test/resources*/**");
                                });
                                excludes.child("exclude",exclude -> {
                                    exclude.content("src/test/projects/**");
                                });
                                excludes.child("exclude",exclude -> {
                                    exclude.content("src/test/remote-repo/**");
                                });
                                excludes.child("exclude",exclude -> {
                                    exclude.content("**/*.odg");
                                });
                            });
                        })
                        .endXML()
                    ) // end of configuration section
                .endPlugin()
                ,
                plugin("org.eclipse.m2e:lifecycle-mapping:1.0.0")
                    .configuration(
                        startXML()
                        .tag("configuration", configuration -> {
                            configuration.child("lifecycleMappingMetadata",lifecycleMappingMetadata -> {
                                lifecycleMappingMetadata.child("pluginExecutions",pluginExecutions -> {
                                    pluginExecutions.child("pluginExecution",pluginExecution -> {
                                        pluginExecution.child("pluginExecutionFilter",pluginExecutionFilter -> {
                                            pluginExecutionFilter.child("groupId",groupId -> {
                                                groupId.content("org.apache.rat");
                                            });
                                            pluginExecutionFilter.child("artifactId",artifactId -> {
                                                artifactId.content("apache-rat-plugin");
                                            });
                                            pluginExecutionFilter.child("versionRange",versionRange -> {
                                                versionRange.content("[0.10,)");
                                            });
                                            pluginExecutionFilter.child("goals",goals -> {
                                                goals.child("goal",goal -> {
                                                    goal.content("check");
                                                });
                                            });
                                        });
                                        pluginExecution.child("action",action -> {
                                            action.child("ignore",ignore -> {
                                            });
                                        });
                                    });
                                });
                            });
                        })
                        .endXML()
                    ) // end of configuration section
                .endPlugin()
                ,
                plugin("org.apache.maven.plugins:maven-checkstyle-plugin:2.14")
                .endPlugin()
            ) //end of plugins section
            .plugins(
                plugin("org.codehaus.mojo:animal-sniffer-maven-plugin:1.14")
                    .configuration(
                        startXML()
                        .tag("configuration", configuration -> {
                            configuration.child("signature",signature -> {
                                signature.child("groupId",groupId -> {
                                    groupId.content("org.codehaus.mojo.signature");
                                });
                                signature.child("artifactId",artifactId -> {
                                    artifactId.content("java17");
                                });
                                signature.child("version",version -> {
                                    version.content("1.0");
                                });
                            });
                        })
                        .endXML()
                    ) // end of configuration section
                .endPlugin()
                ,
                plugin("org.apache.maven.plugins:maven-doap-plugin:1.1")
                    .configuration(
                        startXML()
                        .tag("configuration", configuration -> {
                            configuration.child("asfExtOptions",asfExtOptions -> {
                                asfExtOptions.child("charter",charter -> {
                                    charter.content("The mission of the Apache Maven project is to create and maintain software ...");
                                });
                            });
                        })
                        .endXML()
                    ) // end of configuration section
                .endPlugin()
                ,
                plugin("org.apache.rat:apache-rat-plugin")
                    .configuration(
                        startXML()
                        .tag("configuration", configuration -> {
                            configuration.child("excludes",excludes -> {
                                excludes.attribute("combine.children", "append");
                                excludes.child("exclude",exclude -> {
                                    exclude.content("bootstrap/**");
                                });
                                excludes.child("exclude",exclude -> {
                                    exclude.content("README.bootstrap.txt");
                                });
                                excludes.child("exclude",exclude -> {
                                    exclude.content(".repository/**");
                                });
                                excludes.child("exclude",exclude -> {
                                    exclude.content(".maven/spy.log");
                                });
                                excludes.child("exclude",exclude -> {
                                    exclude.content(".java-version");
                                });
                            });
                        })
                        .endXML()
                    ) // end of configuration section
                .endPlugin()
            ) //end of plugins section
        .endBuild();
        
        profile("apache-release")
            .build(
                profileBuild()
                    .plugins(
                        plugin("org.apache.maven.plugins:maven-assembly-plugin")
                    ) //end of plugins section
                .endBuild()
            ) //end of profile build section
        .endProfile();

        profile("maven-repo-local")
            .activeByDefault(false)
            .activeForPropertyValue("maven.repo.local",null)
            .build(
                profileBuild()
                    .plugins(
                        plugin("org.apache.maven.plugins:maven-surefire-plugin")
                            .configuration(
                                startXML()
                                .tag("configuration", configuration -> {
                                    configuration.child("systemProperties",systemProperties -> {
                                        systemProperties.attribute("combine.children", "append");
                                        systemProperties.child("property",property -> {
                                            property.child("name",name -> {
                                                name.content("maven.repo.local");
                                            });
                                            property.child("value",value -> {
                                                value.content("${maven.repo.local}");
                                            });
                                        });
                                    });
                                })
                                .endXML()
                            ) // end of configuration section
                        .endPlugin()
                    ) //end of plugins section
                .endBuild()
            ) //end of profile build section
        .endProfile();
    }
}