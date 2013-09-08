/*
 * MatAverager_jni.cpp
 *
 *  Created on: Sep 7, 2013
 *      Author: Joe Doyle (Ginto8)
 */
#include "MatAverager_jni.h"
#include <opencv2/core/core.hpp>
using namespace cv;

JNIEXPORT void JNICALL Java_org_on_puz_photobombsquad_MatAverager_average(
   JNIEnv * jenv, jclass, jlong matOut, jobjectarray mats) {
	Mat& out = *((Mat*)matOut);
	int n = GetArrayLength(jenv,mats);
	for(int i=0;i<n;++i) {
		out += *(Mat*)GetObjectArrayElement(jenv,mats,i);
	}
	out /= n;
}
