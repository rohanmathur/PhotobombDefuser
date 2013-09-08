/*
 * MatAverager_jni.cpp
 *
 *  Created on: Sep 7, 2013
 *      Author: Joe Doyle (Ginto8)
 */
#include "MatAverager_jni.h"
#include <opencv2/core/core.hpp>
using namespace cv;

JNIEXPORT void JNICALL Java_org_on_puz_photobombsquad_MatAverager_natAverage(
   JNIEnv * jenv, jclass, jlong matOut, jlongArray mats) {
	Mat& out = *((Mat*)matOut);
	int n = jenv->GetArrayLength(mats);
	long long* arr = jenv->GetLongArrayElements(mats,0);
	for(int i=0;i<n;++i) {
            Mat& m = *(Mat*)arr[i];
            if(i == 0) {
                Mat m2 = m/n;
                m2.copyTo(out);
            } else {
		out += (m/n);
            }
	}
}
