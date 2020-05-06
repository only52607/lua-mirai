
import os

def listClasses(dir,prefix,list):
    subDirs=os.listdir(dir)
    for subDir in subDirs:
        if os.path.isdir(dir + "/" + subDir):
            listClasses(dir + "/" + subDir,prefix + ("." if prefix!="" else "") + subDir.replace("^\w+\.",""),list)
        elif os.path.isfile(dir + "/" + subDir):
            if subDir.find("-info")!=-1:
                continue
            list.append(prefix + ("." if prefix!="" else "") + subDir.replace(".java","") )


srcDir="C:/Users/86182/Desktop/javasrc"
outDir="C:/Users/86182/Desktop/JavaClasses.txt"


packages=os.listdir(srcDir)
list=[]
for package in packages:
    listClasses(srcDir+"/"+package,"",list)
outFile=open(outDir,"w")
for className in list:
    outFile.write(className+"\n")
outFile.close()