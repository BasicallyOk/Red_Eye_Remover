# Red_Eye_Remover
An image processing application made with Java Swing by professor Burton Ma, with a few features I added, the center point being its 3 red eye removal algorithms

RedEye removes red eye by looking for red pixels within a range of colors that can be considered red. 
It's ultimate downfall being it's color tolerance, low tolerance will allow for a ton of red pixels, while high tolernce will delete details in the picture.

RedEyeAdvanced is, well, a bit more advanced, it generates a mask where it thinks the eyes are based on a similar process to RedEye. Afterwards, it uses the greyscaled pixels where the mask is and apply it to the mask, generating a much more believable eye.
However, this approach utilizes pretty much the same detection approach as RedEye for the mask, so although the greyscaled pixels allowed me to crank the tolerance higher, it's not perfect, and takes quite a few seconds to run.
This approach also does not work on small and non-circular eyes.

RedEyeEpic, on the other hand, is a bit more complicated. This method looks for an 3X3 area of pixels that is most likely to be part of an eye, afterwards it will look for a 4X4 area in the vicinity of the 3X3 area, until the average red rgb value is no longer high enough. And then 5X5 and so on.
Afterwards, the tolerance can be as high as I want, without messing up the background.
The issue with this, is that when looking for a 3x3 area, the average value can be fairly finnicky, and thus, inaccurate at times. However, a fix for it is to run it several times on the same picture, to elimate as many red pixels as possible.
It works brilliantly, although takes a bit long. This algorithm will probably benefit from Deep Learning as it tweaks the value.
