package com.reviewmerce.exchange.commonData;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by onebuy on 2015-07-23.
 */
public class Caculator {
    private static Stack stack = new Stack();
    // 연산자 우선순위를 판정 숫자화 반환
    private static int operatorPriority(char operator) {
        if(operator == '(') return 0;
        if(operator == '+' || operator == '-') return 1;
        if(operator == '*' || operator == '/') return 2;
        return 3;
    }
    // 연산자를 표현한 문자인지 검사
    public static boolean isOperator(char ch) {
        return (ch == '+' || ch == '-' || ch == '*' || ch == '/');
    }

    // 숫자를 표현한 문자인지 검사
    public static boolean isNumeric(char ch) {
        return (ch >= '0' && ch <= '9');
    }

    // 중위표현식을 후위표현식으로 변환
    public static String postfix(String expression) throws Exception {
        StringBuilder sb = new StringBuilder();
        char[] exp;
        char ch;

        if(expression == null) {
            throw new NullPointerException("expression is null");
        }

        exp = expression.toCharArray();
        for(int i=0; i<exp.length; i++) {
            if(exp[i] == '(') {
                stack.push(exp[i]);

            } else if(exp[i] == ')') {
                while((ch = (Character)stack.pop()) != '(') {
                    sb.append(ch);
                    sb.append(' ');
                }

            } else if(isOperator(exp[i])) {
                while(!stack.isEmpty() && operatorPriority((Character)stack.peek()) >= operatorPriority(exp[i])) {
                    sb.append(stack.pop());
                    sb.append(' ');
                }
                stack.push(exp[i]);

            } else if(isNumeric(exp[i])) {
                do {
                    sb.append(exp[i++]);
                } while(i<exp.length && isNumeric(exp[i]));
                sb.append(' '); i--;
            }
        }

        while(!stack.isEmpty()) {
            sb.append(stack.pop());
            sb.append(' ');
        }

        return sb.toString();
    }

    // 후위표현식을 계산
    public static double postfixCalc(String expression) throws Exception {
        char[] exp;
        double num;
        if(expression == null) {
            throw new NullPointerException("expression is null");
        }

        exp = expression.toCharArray();
        for(int i=0; i<exp.length; i++) {
            if(isNumeric(exp[i])) {
                num = 0;
                do {
                    num = num * 10 + exp[i++] - '0';
                } while(i < exp.length && isNumeric(exp[i]));
                stack.push(num); i--;



            } else if(exp[i] == '+') {
                stack.push((Double)stack.pop() + (Double)stack.pop());



            } else if(exp[i] == '*') {
                stack.push((Double)stack.pop() * (Double)stack.pop());



            } else if(exp[i] == '-') {
                num = (Double)stack.pop();
                stack.push((Double)stack.pop() - num);

            } else if(exp[i] == '/') {
                num = (Double)stack.pop();
                stack.push((Double)stack.pop() / num);
            }

        }
        return (Double)stack.pop();
    }


    private Map<String,Integer> hashMap = new HashMap<String,Integer>();
    public Caculator()
    {
        hashMap.put("+",0);
        hashMap.put("-",0);
        hashMap.put("*",1);
        hashMap.put("/",1);
        hashMap.put("(",-1);
    }
    public Queue<String> transformPostfix(String param)
    {
        if(param==null || param.trim().equals(""))
            return null;
        Stack <String> stack = new Stack<String>();

        StringBuilder postfixStr = new StringBuilder();

        Queue<String> postfixQue = new LinkedList<String>();

        //Pattern p = Pattern.compile("[0-9]+|\\(|\\)|\\+|\\-|\\*|\\/");
        //Pattern p = Pattern.compile("[0-9]*\\.?[0-9]*+|\\(|\\)|\\+|\\-|\\*|\\/");
        //Pattern p = Pattern.compile("\\d*(\\.?\\d*)$+|\\(|\\)|\\+|\\-|\\*|\\/");
        Pattern p = Pattern.compile("[0-9]+(\\.[0-9]+)?|[\\+\\-\\*\\/\\(\\)]");


        Matcher m = p.matcher(param);

        while(m.find())
        {
            String word = m.group();
            if(word.equals("("))
            {
                stack.push(word);
            }
            else if(hashMap.containsKey(word))
            {
                while(true)
                {
                    if(stack.isEmpty() || hashMap.get(stack.peek()) < hashMap.get(word))
                    {
                        stack.push(word);
                        break;
                    }
                    else
                    {
                        String popStr = stack.pop();
                        postfixStr.append(popStr);
                        postfixQue.add(popStr);
                    }
                }
            }
            else if(word.equals(")"))
            {
                while(true)
                {
                    String popStr = stack.pop();
                    if(popStr.equals("("))
                    {
                        break;
                    }
                    else
                    {
                        postfixStr.append(popStr);
                        postfixQue.add(popStr);
                    }
                }
            }
            else
            {
                postfixStr.append(word);
                postfixQue.add(word);
            }

        }
        while(stack.isEmpty() == false)
        {
            String popStr = stack.pop();
            postfixStr.append(popStr);
            postfixQue.add(popStr);
        }
        return postfixQue;
    }
    public Double caculatePostfix(Queue<String> param)
    {
        Stack<Double> stack = new Stack<Double>();

        Double firstVal;
        Double secondVal;

        while(param.isEmpty() == false)
        {
            String word = param.remove();

            if(hashMap.containsKey(word))
            {
                secondVal = stack.pop();
                firstVal = stack.pop();

                switch (word.charAt(0))
                {
                    case '+':
                        stack.push(firstVal + secondVal);
                        break;
                    case '-':
                        stack.push(firstVal - secondVal);
                        break;
                    case '*':
                        stack.push(firstVal * secondVal);
                        break;
                    case '/':
                        stack.push(firstVal / secondVal);
                        break;
                    default:
                        break;
                }

            }
            else
            {
                stack.push(Double.valueOf(word));
            }
        }
        return stack.pop();
    }

}
