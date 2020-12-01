# read -p "Enter audio port: " PORT_AUDIO
# read -p "Enter video port: " PORT_VIDEO
PORT_AUDIO=7004
PORT_VIDEO=7006
REQUEST="ffmpeg -re -stream_loop -1 -i ./ford.mp4 \
 -vcodec libx264 -preset veryfast -profile:v baseline -level:v 5.1 -b:v 1500k -x264-params keyint=20:scenecut=0 -an -f rtp rtp://localhost:${PORT_VIDEO} \
 -acodec libopus -vn -f rtp rtp://localhost:${PORT_AUDIO} 2>&1"
echo $REQUEST
eval $REQUEST


# Convert any video to the optimized format
# ffmpeg -i /opt/video5.mp4 -vcodec libx264 -profile:v baseline -level:v 5.1 -b:v 1500k -x264-params keyint=20:scenecut=0 -acodec libopus -strict -2 ./video-v1.mp4
# ffmpeg -i /opt/video3.mp4 -vcodec libx264 -profile:v baseline -level:v 5.1 -b:v 1500k -x264-params keyint=20:scenecut=0 -acodec libopus -strict -2 ./video-v2.mp4

# ffmpeg -i /opt/video5.mp4 -vcodec libx264 -profile:v baseline -level:v 5.2 -x264-params keyint=20:scenecut=0 -acodec libopus -strict -2 ./video-v1.mp4
# ffmpeg -i /opt/video3.mp4 -vcodec libx264 -profile:v baseline -level:v 5.2 -x264-params keyint=20:scenecut=0 -acodec libopus -strict -2 ./video-v2.mp4