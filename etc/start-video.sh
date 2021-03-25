#!bash
PORT_VIDEO=$1
REQUEST="ffmpeg -re -stream_loop -1 -i ./MGB02.mp4 \
 -vcodec libx264 -preset veryfast -profile:v baseline -level:v 5.1 -b:v 1500k -x264-params keyint=20:scenecut=0 -an -f rtp rtp://172.17.0.1:${PORT_VIDEO} \
 2>&1"
echo $REQUEST
eval $REQUEST

