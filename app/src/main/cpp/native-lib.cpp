#include <jni.h>
#include <string>
#include <vector>


extern "C"
JNIEXPORT void JNICALL
Java_com_example_imageprocessapp_MainActivityKt_nativeApplyGrayscale(JNIEnv *env, jclass clazz,
                                                                     jintArray pixels, jint width,
                                                                     jint height) {
    // Access the pixel array elements
    jint *pixelData = env->GetIntArrayElements(pixels, nullptr);

    // Loop through each pixel in the array
    for (int i = 0; i < width * height; i++) {
        // Extract the current pixel's color.
        jint color = pixelData[i];

        // Extract RGB components
        jint red = (color >> 16) & 0xFF;
        jint green = (color >> 8) & 0xFF;
        jint blue = color & 0xFF;

        // Calculate grayscale value
        jint grayscale = (0.299 * red + 0.587 * green + 0.114 * blue);

        // Set the new pixel color to grayscale
        pixelData[i] = (0xFF << 24) | (grayscale << 16) | (grayscale << 8) | grayscale;
    }
    // Release the pixel array elements back to Java
    env->ReleaseIntArrayElements(pixels, pixelData, 0);
}