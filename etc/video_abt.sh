PORT_VIDEO=$1
SRC_FILE=$2
JANUS_IP=$3
ffmpeg -re -stream_loop -1 -i $2 -vcodec libx264 -preset veryfast -profile:v baseline -level:v 5.1 -b:v 1500k -x264-params keyint=20:scenecut=0 -an -f rtp rtp://${JANUS_IP}:${PORT_VIDEO}
