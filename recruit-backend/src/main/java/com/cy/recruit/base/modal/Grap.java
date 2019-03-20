package com.cy.recruit.base.modal;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Grap {

    private int x;
    private int y;
    private int weight;
    private Grap next;

    public Grap(int x, int y, int weight){
     this.x = x;
     this.y = y;
     this.weight = weight;
    }

    public Grap(){

    }

    public static Grap cal(Grap grap, Grap[][] graps, int maxX, int maxY, int[] a, int n){
        if(grap.getX() >= maxX){
            if(grap.getY() >= maxY){
                return grap;
            }else{
                Grap next = new Grap();
                next.setX(grap.getX());
                next.setY(grap.getY() + 1);
                next.setWeight(graps[grap.getX()][grap.getY()+1].getWeight());
                grap.setNext(cal(next, graps, maxX, maxY, a, n+1));
                return grap;
            }
        }else{
            if(grap.getY() >= maxY){
                Grap next = new Grap();
                next.setX(grap.getX() + 1);
                next.setY(grap.getY());
                next.setWeight(graps[grap.getX()+1][grap.getY()].getWeight());
                grap.setNext(cal(next, graps, maxX, maxY, a, n+1));
                return grap;
            }else{
                Grap next = new Grap();
                if(a[n] == 1){//x+1
                    next.setX(grap.getX()+1);
                    next.setY(grap.getY());
                    next.setWeight(graps[grap.getX()+1][grap.getY()].getWeight());
                }else{//y+1
                    next.setX(grap.getX());
                    next.setY(grap.getY() + 1);
                    next.setWeight(graps[grap.getX()][grap.getY()+1].getWeight());
                }
                grap.setNext(cal(next, graps, maxX, maxY, a, n+1));
                return grap;
            }
        }
    }

    public static void main(String[] args){
        int maxX = 3, maxY = 3;
        int count = maxX + maxY;
        int[] a = new int[count];
        for(int i=0;i<maxX;i++){
            a[i] = 1;
        }
        Grap[][] grapArr = new Grap[maxX+1][maxY+1];
        for(int i=0; i<=maxX; i++){
            for(int j=0; j<=maxY; j++){
                int weight = (int)(-5+Math.random()*(5-(-5)+1));
                grapArr[i][j] = new Grap(i,j, weight);
            }
        }

        List<Grap> graps = new ArrayList<>();
        Grap grap = new Grap(0,0,grapArr[0][0].getWeight());
        System.out.println(Arrays.toString(a));
        graps.add(cal(grap, grapArr, maxX, maxY, a, 0));
        for(int i= maxX; i<a.length; i++){
            int j = i;
            while(j > 0 && a[j-1] > 0){
                int temp = a[j-1];
                a[j-1] = a[j];
                a[j] = temp;
                j--;
                System.out.println(Arrays.toString(a));
                Grap grap1 = new Grap(0,0,grapArr[0][0].getWeight());
                graps.add(cal(grap1, grapArr, maxX, maxY, a, 0));
            }
        }
        for(Grap g : graps){
            int allWeight = g.getWeight();
            System.out.print("("+g.getX()+","+g.getY()+","+g.getWeight()+")");
            while(g.getNext()!=null){
                g = g.getNext();
                System.out.print("("+g.getX()+","+g.getY()+","+g.getWeight()+")");
                allWeight += g.getWeight();
            }
            System.out.println("," + allWeight);
        }
    }
}
