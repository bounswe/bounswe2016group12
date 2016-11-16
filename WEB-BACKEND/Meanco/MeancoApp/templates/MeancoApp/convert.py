import os
"""Script for traversing files in directory to convert slashes to 
backslashes. This is for non windows users.
"""
for file in os.listdir():
  if file.endswith('.html'):
    fr=open(file,"r")
    li=fr.readlines()
    fr.close()
    fw=open(file,"w")
    for line in li:
      if line.find("include")>-1 or line.find("extends")>-1:
        fw.write(line.replace("\\","/"))
      else:
        fw.write(line)
    fw.close()


     
