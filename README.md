jove
====
*J*ava *O*penCV *V*isualization *E*ngine
--------------------------------
 -or-
 
*j*Monkey *O*penC*V* *E*nvironment
--------------------------
 -or- (recursively) 
 
*J*ove *O*penC*V* *E*xperiments
------------------------

### OpenCV visualization in jMonkey

Process video streams, parallelized, with ease.

See the (Scala) examples in com.metaquanta.jove.examples for usage.

To run the examples:

    $ sbt run

The following VM options are suggested:

    JAVA_OPTS="-XX:+CMSClassUnloadingEnabled -Xms512m -Xmx512m -XX:MaxDirectMemorySize=2048M -XX:MaxPermSize=384m -XX:ReservedCodeCacheSize=192m -Dfile.encoding=UTF8"

Most are merely taken from sbt defaults. However, we are intentionally lowering the heap size while raising the direct memory size. The lower heap value is needed to prompt the garbage collector to more aggressively free direct memory.

This software links to OpenCV and includes a binary from OpenCV in 'lib'. The following is as required by OpenCV's license, the three-clause BSD license:

> Copyright (C) 2000-2018, Intel Corporation, all rights reserved.  
> Copyright (C) 2009-2011, Willow Garage Inc., all rights reserved.  
> Copyright (C) 2009-2016, NVIDIA Corporation, all rights reserved.  
> Copyright (C) 2010-2013, Advanced Micro Devices, Inc., all rights reserved.  
> Copyright (C) 2015-2016, OpenCV Foundation, all rights reserved.  
> Copyright (C) 2015-2016, Itseez Inc., all rights reserved.  
> Third party copyrights are property of their respective owners.
>
> Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
> * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
> * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
> * Neither the names of the copyright holders nor the names of the contributors may be used to endorse or promote products derived from this software without specific prior written permission.
>
> This software is provided by the copyright holders and contributors "as is" and any express or implied warranties, including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are disclaimed. In no event shall copyright holders or contributors be liable for any direct, indirect, incidental, special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability, whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use of this software, even if advised of the possibility of such damage.
