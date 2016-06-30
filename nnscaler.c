#include <jni.h>
#include <stdio.h>
#include <string.h>
#include "me_lcw_jil_scalers_NearestNeighborScaler.h"


JNIEXPORT void JNICALL Java_me_lcw_jil_scalers_NearestNeighborScaler_scaleJil(JNIEnv *env, jobject jobj, jbyteArray srcArray, jint srcWidth, jint srcHeight, jbyteArray newArray, jint newWidth, jint newHeight, jint colors) {

  jbyte* srcArrayPtr = (*env)->GetByteArrayElements(env, srcArray, 0);
  jbyte* newArrayPtr = (*env)->GetByteArrayElements(env, newArray, 0);
  float x_ratio = srcWidth/(float)newWidth;
  float y_ratio = srcHeight/(float)newHeight;
  int px = 0;
  int py = 0;
  int y = 0;
  int x = 0;

  for (y=0; y<newHeight; y++) {
    py = (int) (y*y_ratio);
    for(x=0; x<newWidth; x++) {
      px = (int)(x*x_ratio);
      int sp = ((srcWidth*py)+px)*(colors);
      int np = ((newWidth*y)+x)*(colors);
      memcpy(newArrayPtr+np, srcArrayPtr+sp, colors);
    }
  }

  (*env)->SetByteArrayRegion(env, newArray, 0, newWidth*newHeight*colors, newArrayPtr);

}
