package com.neuronet.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Neuron {

    private static final Logger logger = LoggerFactory.getLogger(Neuron.class);

    private final List<Edge> inputEdgeList = new ArrayList<Edge>();
    private final List<Edge> outputEdgeList = new ArrayList<Edge>();

    public static float param = 0.2f;

    private float[] w;
    private int typeofAH = -1;
    private float s = 0;
    private float h = 0;
    private float b = 0;
    private float alfa = 0;

    public Neuron(float[] _w, float _b, int _toAH, float _a) {
        this.w = _w;
        this.b = _b;
        this.alfa = _a;
        this.typeofAH = _toAH;
        this.s = 0;
    }

    public float[] getW() {
        final float[] w = new float[inputEdgeList.size()];
        for (int i = 0, wLength = w.length; i < wLength; i++) {
            w[i] = inputEdgeList.get(i).getWeight();
        }
        return w;
    }

    public void setW(float[] _w) {
        this.w = _w;
    }

    //  Расчет всего и вся =)
    public float getS(float[] _s) {
        this.h = this.summ(_s);
        switch (this.typeofAH) {
            //  линейность
            case 0: {
                this.s = h;
                break;
            }
            //  биполярность
            case 1: {
                if (h > 0) {
                    this.s = 1;
                } else {
                    this.s = -1;
                }
                break;
            }
            //  бинарность
            case 2: {
                if (h > 0) {
                    this.s = 1;
                } else {
                    this.s = 0;
                }
                break;
            }
            //  биполярная сигмоида
            case 3: {
                this.s = h / (this.alfa + Math.abs(h));
                break;
            }
            //  бинарная сигмоида
            case 4: {
                this.s = (float) (h / (1 + Math.exp(-1 * this.alfa * h)));
                break;
            }
            //  гауссиана
            case 5: {
                this.s = (float) (Math.exp(-0.5 * h * h));
                break;
            }
            default: {
                this.s = 0;
                break;
            }
        }
        return this.s;
    }

    public float getF() {
        float f;
        switch (this.typeofAH) {
            //  линейность
            case 0: {
                f = h;
                break;
            }
            //  биполярность
            case 1: {
                if (h > 0) {
                    f = 1;
                } else {
                    f = -1;
                }
                break;
            }
            //  бинарность
            case 2: {
                if (h > 0) {
                    f = 1;
                } else {
                    f = 0;
                }
                break;
            }
            //  биполярная сигмоида
            case 3: {
                f = (this.alfa - h * Math.abs(h) + Math.abs(h)) / ((this.alfa + Math.abs(h)) * (this.alfa + Math.abs(h)));
                break;
            }
            //  бинарная сигмоида
            case 4: {
                f = (float) ((1 + Math.exp(-1 * this.alfa * h) + this.alfa * h * Math.exp(-1 * this.alfa * h)) / ((1 + Math.exp(-1 * this.alfa * h)) * (1 + Math.exp(-1 * this.alfa * h))));
                break;
            }
            //  гауссиана
            case 5: {
                f = (float) (-h * Math.exp(-0.5 * h * h));
                break;
            }
            default: {
                f = 0;
                break;
            }
        }
        return f;
    }

    //  Обучение
    public float[] educate(float error, float[] _s) {
        float[] tmp = new float[this.w.length - 1];

        //  подсчитали ошибkу
        for (int i = 1; i < this.w.length; i++) {
            tmp[i - 1] = this.w[i] * error;
        }
/*
            for (int i = 1; i < this.w.Length; i++)
            {
                tmp[i - 1] = this.w[i] * error;
            }
            */

        //  изменили синапсы
        this.edicateW(error, _s);

        return tmp;
    }

    //  Обучение( Подсройка синапсов)
    private void edicateW(float error, float[] _s) {
        this.w[0] += error * param;

        for (int i = 1; i < this.w.length; i++) {
            this.w[i] += _s[i - 1] * error * param;
        }
    }

    //  Скалярное произведение двух векторов
    private float summ(float[] _s) {
        float temp = 0;

        //  смещение
        temp += this.b * this.w[0];

        //  все остальное
        for (int i = 0; i < _s.length; i++) {
            temp += _s[i] * this.w[i + 1];
        }

        return temp;
    }
}
