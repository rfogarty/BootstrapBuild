# BootstrapBuild - Package Management and Build Automation
-----------------------------------------------------------
BootstrapBuild is a tool to manage complicated product builds. It acts as
a sort of package manager of sorts such as pacman, apt, emerge, yum, brew, etc.
However, unlike package managers, the installed products may have to be
built from source.

Additionally, BootstrapBuild has some Maven like features and can create
a package to be installed on another machine. Unlike Maven, the created
package is assumed to need to be built on the target installation machine.
In BootstrapBuild vernacular a package is called a "Carton". 

A bit tongue-in-cheek, BootstrapBuild uses many terms that elude to the 
chicken and egg problem of installing products, thus the reader may find
BootstrapBuild is full of fowl language. BootstrapBuild cracks the chicken-
and-egg problem by separating "automation" tasks outside of the product itself. Some
terms that are used that will be explained later include: hatch(ed),
nest, brood, preen, incubate, migrate, pluck, lay, etc.

