package com.neuronet;

import com.neuronet.common.Net;
import com.neuronet.common.Neuron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NeuroNet {

    private static final Logger logger = LoggerFactory.getLogger(NeuroNet.class);

    public static void main(String... args) {
        logger.info("NeuroNet is starting.");
        create();
        byte[] rawBytes = GetRawBytes();
        float[] input = new float[rawBytes.length];
        for (int i = 0; i < input.length; i++) {
            input[i] = rawBytes[i];
        }
        for (int i = 0; i < 10000; i++) {
            logger.debug("run:" + Arrays.toString(newnet.runNet(input)));
            newnet.educate(new float[]{-1, -1, 1}, input);
        }
        logger.debug(Arrays.toString(testNet(newnet)));
    }

    private static String textBox1;
    private static String textBox2;
    private static String textBox3;
    private static float textBox4 = 0.005f;

    public static String mess = "";
    public static float norma = 5000;
    public static int rand = 200;
    public static float div = 100;
    public static boolean flag = true;
    public static Net newnet;

    public static int position = 25;
    public static int pic = 0;

    private static byte[] GetRawBytes() {
        return new byte[]{1, 2, 3};
    }

    private static float getNorm(float[] _x) {
        //  находим норму:
        float sum = 0;
        for (float d : _x) {
            sum += d * d;
        }
        return (float) Math.sqrt(sum);
    }

    private static int sum(int... i) {
        int sum = 0;
        for (int s : i) {
            sum += s;
        }
        return sum;
    }

    private static int[] testNet(Net mynet) {
        float[] _rez;
        List<Float> rt = new ArrayList<Float>();

        byte[] rawBytes = GetRawBytes();

        float[] _x = new float[rawBytes.length];
        for (int i = 0; i < _x.length; i++) {
            _x[i] = rawBytes[i];
        }

        //  находим норму:
        NeuroNet.norma = getNorm(_x);

        //  нормируем:
        for (int i = 0; i < _x.length; i++) {
            _x[i] = rawBytes[i] / NeuroNet.norma;
        }

        _rez = mynet.runNet(_x);

        for (float d : _rez) {
            rt.add(d);
        }

        int[] error = new int[2];

        for (Float aRt : rt) {
            if (aRt > 0) {
                error[0]++;
                error[1]++;
            }
        }

        return error;
    }

    private static void create() {
        NeuroNet.mess = "";
        byte[] rawBytes = GetRawBytes();

        float[] _x = new float[rawBytes.length];
        for (int i = 0; i < _x.length; i++) {
            _x[i] = rawBytes[i];
        }

        //  находим норму:            
        NeuroNet.norma = getNorm(_x);

        //  нормируем:
        for (int i = 0; i < _x.length; i++) {
            _x[i] = (rawBytes[i]) / NeuroNet.norma;
        }

        newnet = new Net();
//        newnet.setLayer(0, 300, rawBytes.length, 3, 0.007f);
//        newnet.setLayer(1, 50, 300, 3, 0.01f);
//        newnet.setLayer(2, 36, 50, 3, 0.1f);

//        newnet.setLayer(0, 10, rawBytes.length, 3, 0.0005f);
//        newnet.setLayer(1, 5, 10, 3, 0.0001f);
//        newnet.setLayer(2, 3, 5, 3, 0.0001f);

        newnet.addLayer(10, rawBytes.length, 3, 0.0005f);
        newnet.addLayer(5, 10, 3, 0.0001f);
        newnet.addLayer(3, 5, 3, 0.0001f);

        NeuroNet.mess += "Созданна сеть " + NeuroNet.norma + "\n";
    }

    private void button4_Click() {
        byte[] rawBytes;

        rawBytes = GetRawBytes();

        float[] _x = new float[rawBytes.length];
        for (int i = 0; i < _x.length; i++) {
            _x[i] = rawBytes[i];
        }

        //  находим норму:
        NeuroNet.norma = getNorm(_x);

        //  нормируем:
        for (int i = 0; i < _x.length; i++) {
            _x[i] = rawBytes[i] / NeuroNet.norma;
        }

        Net mynet = ((Net) NeuroNet.newnet);

        float[] _rez = mynet.runNet(_x);

        float max = -2;
        int maxindex = 0;
        int index = 0;

        for (float d : _rez) {
            if (max < d) {
                max = d;
                maxindex = index;
            }
            index++;
        }
        NeuroNet.mess += maxindex + " (" + max + ")\n";
    }

    private void educate(float[] _sigma) {
        byte[] rawBytes = GetRawBytes();

        float[] _x = new float[rawBytes.length];
        for (int i = 0; i < _x.length; i++) {
            _x[i] = rawBytes[i];
        }

        //  находим норму:            
        NeuroNet.norma = getNorm(_x);

        //  нормируем:
        for (int i = 0; i < _x.length; i++) {
            _x[i] = rawBytes[i] / NeuroNet.norma;
        }

        Random rnd = new Random(System.currentTimeMillis());

        while (flag) {

            int q = rnd.nextInt(5);
            Net mynet = new Net();

            //  число нейронов
            int n = rnd.nextInt(100);

            //  число нейронов пред слоя
            int n1 = rawBytes.length;

            //  АХ
            int ah = rnd.nextInt(2) + 3;
            ah = 3;

            //  параметр
            float p = rnd.nextInt(100) / 1000.0f;

            for (int j = 0; j < q - 1; j++) {
                mynet.setLayer(j, n, n1, ah, p);

                n1 = n;
                n = rnd.nextInt(50 - j * 10);
                ah = rnd.nextInt(2) + 3;
                ah = 3;
                p = rnd.nextInt(100) / 100.0f;
            }

            mynet.setLayer(q - 1, 1, n1, ah, p);

            //  ОБУЧЕНИЕ
            Neuron.param = textBox4;

            float[] _rez1 = new float[1];
            _rez1[0] = 1;
            float[] _rez2 = new float[1];
            _rez2[0] = 2;

            for (int k = 0; k <= 10; k++) {
                if ((k == 0) || (k == 5)) {
                    int[] er1 = testNet(mynet);
                    int s = er1[0] * er1[0] + er1[1] * er1[1];
                    if (s == 2500) {
                        break;
                    }
                }

                int count = 0;
                for (String s : new String[]{""}) {
                    NeuroNet.mess = "Эпоха: " + k + "\n";
                    NeuroNet.mess += "Примеров пройденно уже " + count;

                    rawBytes = GetRawBytes();

                    _x = new float[rawBytes.length];

                    for (int i = 0; i < _x.length; i++) {
                        _x[i] = (rawBytes[i]);
                    }

                    //  находим норму:            
                    NeuroNet.norma = getNorm(_x);

                    //  нормируем:
                    for (int i = 0; i < _x.length; i++) {
                        _x[i] = (float) (rawBytes[i]) / NeuroNet.norma;
                    }

                    _rez1 = mynet.educate(_sigma, _x);
                    if (_rez2[0] == _rez1[0]) {
                        k = 11;
                        break;
                    } else {
                        _rez2 = _rez1;
                    }
                }
            }

            int[] er = testNet(mynet);

            if (sum(er) < 35) {
                NeuroNet.flag = false;
            }
        }
    }

    private void button9_Click() {
        Neuron.param = (float) (textBox4);

        String path = textBox2;
        String datapath = textBox3;
        String file;

        //Net mynet = new Net(path);
        Net mynet = ((Net) NeuroNet.newnet);

        int count = 0;

        for (String s : new String[]{""}) {
            logger.debug("Примеров пройденно уже " + count);


            file = datapath + "\\" + s;
            byte[] rawBytes;

            for (int j = 0; j < 4; j++) {
                NeuroNet.position += 15;
                if (NeuroNet.position > 75) {
                    NeuroNet.position = 25;
                    NeuroNet.pic++;
                }

                rawBytes = GetRawBytes();

                float[] _x = new float[rawBytes.length];

                for (int i = 0; i < _x.length; i++) {
                    _x[i] = (float) (rawBytes[i]);
                }

                NeuroNet.mess += rawBytes.length + "\n";

                //  находим норму:            
                NeuroNet.norma = getNorm(_x);

                //  нормируем:
                for (int i = 0; i < _x.length; i++) {
                    _x[i] = (float) (rawBytes[i]) / NeuroNet.norma;
                }

                float[] _sigma;
                _sigma = new float[36];
                for (int i = 0; i < _sigma.length; i++)
                    _sigma[i] = -1;

                file = file.substring(0, file.length() - 3) + "txt";

                /*
                using(System.IO.StreamReader myfile = new System.IO.StreamReader( @ "" + file, true))
                {
                    String tmp = "";
                    String cap = "";
                    while (tmp != null) {
                        cap = tmp;
                        tmp = myfile.ReadLine();
                    }

                    char c = cap[j];

                    int code = 0;
                    code = c + 0;

                    if (code < 58) {
                        _sigma[code - 48] = 1;
                    } else {
                        _sigma[code - 97 + 10] = 1;
                    }
                }
                           */

                mynet.educate(_sigma, _x);
            }
            count++;
        }
    }

    private void button11_Click() {
        Neuron.param = (float) (textBox4);

        String path = textBox2;
        String datapath = textBox3;
        String file;

        Net mynet = ((Net) NeuroNet.newnet);

        int count = 0;

        for (String s : new String[]{""}) {
            logger.debug("Примеров пройденно уже " + count);

            file = datapath + "\\" + s;

            byte[] rawBytes;


            for (int j = 0; j < 4; j++) {
                switch (NeuroNet.position) {
                    case 25: {
                        NeuroNet.position = 40;
                        break;
                    }
                    case 40: {
                        NeuroNet.position = 60;
                        break;
                    }
                    case 60: {
                        NeuroNet.position = 75;
                        break;
                    }
                    case 75: {
                        NeuroNet.position = 25;
                        NeuroNet.pic++;
                        break;
                    }
                }

                rawBytes = GetRawBytes();

                float[] _x = new float[rawBytes.length];

                for (int i = 0; i < _x.length; i++) {
                    _x[i] = (float) (rawBytes[i]);
                }

                //  находим норму:            
                NeuroNet.norma = getNorm(_x);
                //  нормируем:
                for (int i = 0; i < _x.length; i++) {
                    _x[i] = (float) (rawBytes[i]) / NeuroNet.norma;
                }

                float[] _sigma;
                _sigma = new float[36];
                for (int i = 0; i < _sigma.length; i++)
                    _sigma[i] = -1;

                file = file.substring(0, file.length() - 3) + "txt";

                /*
                using(System.IO.StreamReader myfile = new System.IO.StreamReader( @ "" + file, true))
                {
                    String tmp = "";
                    String cap = "";
                    while (tmp != null) {
                        cap = tmp;
                        tmp = myfile.ReadLine();
                    }

                    char c = cap[j];

                    int code = 0;
                    code = c + 0;

                    if (code < 58) {
                        _sigma[code - 48] = 1;
                    } else {
                        _sigma[code - 97 + 10] = 1;
                    }
                }       */

                mynet.educate(_sigma, _x);
            }
            count++;
        }
    }
}
