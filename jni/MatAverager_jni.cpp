/*
 * MatAverager_jni.cpp
 *
 *  Created on: Sep 7, 2013
 *      Author: Joe Doyle (Ginto8)
 */
#include <jni.h>
#include "MatAverager_jni.h"
#include <opencv2/core/core.hpp>
using namespace cv;

JNIEXPORT void JNICALL Java_org_on_puz_photobombsquad_MatAverager_average(
   JNIEnv * jenv, jclass, jlong matOut, jobjectArray mats) {
	Mat& out = *((Mat*)matOut);
	int n = jenv->GetArrayLength((jarray)mats);
	for(int i=0;i<n;++i) {
		out += *(Mat*)jenv->GetObjectArrayElement(mats,i);
	}
	out /= n;
}
