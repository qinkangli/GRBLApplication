package com.example.grblapplication;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Administrator on 2019/2/14 0014.
 */

public class GraycoderCore
{
    public static ArrayList<String> convertToGCode(float[][] paramArrayOfFloat, int paramInt1, int paramInt2, int paramInt3, double paramDouble)
    {
        ArrayList localArrayList = new ArrayList();

        String str1 = String.format("G0 X0 Y0 F%d %n", new Object[] { Integer.valueOf(paramInt1) });
        localArrayList.add(str1);
        String str3;
        for (int i = 0; i < paramArrayOfFloat.length; i++)
        {
            String str2 = String.format("G0 X0 Y%.3f F%d %n", new Object[] { Double.valueOf(paramDouble * i), Integer.valueOf(paramInt1) });
            localArrayList.add(str2);

            for (int k = 0; k < paramArrayOfFloat[0].length; k++)
            {
                str3 = String.format("G1 X%.3f Y%.3f F%d S%.3f %n", new Object[] { Double.valueOf(paramDouble * (k + 1)), Double.valueOf(paramDouble * i), Integer.valueOf(paramInt2), Float.valueOf(paramArrayOfFloat[i][k]) });
                localArrayList.add(str3);
            }

        }

        int i = paramArrayOfFloat.length;
        int j = paramArrayOfFloat[0].length;

        for (int k = 0; k < paramInt3; k++)
        {
            str3 = String.format("G1 X%d Y0 F%d S1.0 %n", new Object[] { Double.valueOf(paramDouble * j), Integer.valueOf(paramInt2) });
            String str4 = String.format("G1 X%d Y%d F%d S1.0 %n", new Object[] { Double.valueOf(paramDouble * j), Double.valueOf(paramDouble * i), Integer.valueOf(paramInt2) });
            String str5 = String.format("G1 X0 Y%d F%d S1.0 %n", new Object[] { Double.valueOf(paramDouble * i), Integer.valueOf(paramInt2) });
            String str6 = String.format("G1 X0 Y0 F%d S1.0 %n", new Object[] { Integer.valueOf(paramInt2) });
            localArrayList.add(str3);
            localArrayList.add(str4);
            localArrayList.add(str5);
            localArrayList.add(str6);
        }

        localArrayList.add(str1);

        return localArrayList;
    }

    public static float[][] convertToGray(Bitmap paramBufferedImage)
    {
        int i = paramBufferedImage.getHeight();
        int j = paramBufferedImage.getWidth();

        float[][] arrayOfFloat = new float[i][j];

        for (int k = 0; k < i; k++)
        {
            for (int m = 0; m < j; m++)
            {
                int Pixel = paramBufferedImage.getPixel(m,k);
                float[] arrayOfFloat1 = RGBTOHSB((Pixel & 0xff0000) >> 16,(Pixel & 0xff00) >> 8, (Pixel & 0xff));
                arrayOfFloat[k][m] = arrayOfFloat1[2];
            }

        }

        return arrayOfFloat;
    }


    public static float[] RGBTOHSB(int rgbR, int rgbG, int rgbB) {
        assert 0 <= rgbR && rgbR <= 255;
        assert 0 <= rgbG && rgbG <= 255;
        assert 0 <= rgbB && rgbB <= 255;
        int[] rgb = new int[] { rgbR, rgbG, rgbB };
        Arrays.sort(rgb);
        int max = rgb[2];
        int min = rgb[0];

        float hsbB = max / 255.0f;
        float hsbS = max == 0 ? 0 : (max - min) / (float) max;

        float hsbH = 0;
        if (max == rgbR && rgbG >= rgbB) {
            hsbH = (rgbG - rgbB) * 60f / (max - min) + 0;
        } else if (max == rgbR && rgbG < rgbB) {
            hsbH = (rgbG - rgbB) * 60f / (max - min) + 360;
        } else if (max == rgbG) {
            hsbH = (rgbB - rgbR) * 60f / (max - min) + 120;
        } else if (max == rgbB) {
            hsbH = (rgbR - rgbG) * 60f / (max - min) + 240;
        }

        return new float[] { hsbH, hsbS, hsbB };
    }

    public static float[][] convertToPower(float[][] paramArrayOfFloat, float paramFloat1, float paramFloat2)
    {
        float f1 = paramFloat2 - paramFloat1;

        float[][] arrayOfFloat = new float[paramArrayOfFloat.length][paramArrayOfFloat[0].length];

        for (int i = 0; i < paramArrayOfFloat.length; i++)
        {
            for (int j = 0; j < paramArrayOfFloat[0].length; j++)
            {
                float f2 = (1.0F - paramArrayOfFloat[i][j]) * f1 + paramFloat1;
                arrayOfFloat[i][j] = f2;
            }

        }

        return arrayOfFloat;
    }


    public static void reflectX(float[][] paramArrayOfFloat)
    {
        int i = paramArrayOfFloat.length;
        int j = paramArrayOfFloat[0].length;

        float[][] arrayOfFloat = new float[i][j];
        int m;
        for (int k = 0; k < i; k++)
        {
            for (m = 0; m < j; m++)
            {
                arrayOfFloat[k][(j - 1 - m)] = paramArrayOfFloat[k][m];
            }

        }

        for (int k = 0; k < i; k++)
        {
            for (m = 0; m < j; m++)
            {
                paramArrayOfFloat[k][m] = arrayOfFloat[k][m];
            }
        }
    }

    public static void writeToFile(File paramFile, ArrayList<String> paramArrayList)
    {
        try
        {
            FileWriter localFileWriter = new FileWriter(paramFile);

            for (String str : paramArrayList)
            {
                localFileWriter.write(str);
            }

            localFileWriter.close();
        }
        catch (IOException localIOException)
        {
            System.out.println("Error! Cannot write to output file.");
        }
    }
}