import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by ShuaishuaiCui on 2016/10/11.
 */
public class AnimalsChess {

    public static void main(String[] args) throws FileNotFoundException {
        File tile = new File("tile.txt");
        Scanner fileTile = new Scanner(tile);
        char[][] mapData = new char[7][9];
        char[][] animalData = new char[7][9];
        char[][][] theMapOfEveryStep = new char[999][7][9];
        String dataMap = " ";
        File animal = new File("animal.txt");
        Scanner fileAnimal = new Scanner(animal);
        String dataAnimal = " ";
        for (int i = 0; i < 7; i++) {
            dataMap = fileTile.nextLine();
            dataAnimal = fileAnimal.nextLine();
            for (int j = 0; j < 9; j++) {
                mapData[i][j] = dataMap.charAt(j);
                animalData[i][j] = dataAnimal.charAt(j);
            }
        }
        System.out.println("\n输入help以得到帮助和规则说明\n");
        printMap(mapData, animalData);

        int num = 0;
        int numMax = 0;//TODO:为了去判断悔棋和撤销悔棋能否进行，并且用来完成悔棋这个功能
        theMapOfEveryStep(num, theMapOfEveryStep, animalData);//TODO:记录初始地图。

        Scanner reception = new Scanner(System.in);
        while (true) {
            if (player) {
                System.out.println("It turns left: ");
            } else System.out.println("It turns right: ");
            String order = reception.next();
            if (order.equals("undo")) {
                //regret his order
                if (num == 0) {
                    System.out.println("已经退回到开局了，无法悔棋了");
                    continue;
                }
                num--;
                printMap(mapData, theMapOfEveryStep[num]);
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 9; j++) {
                        animalData[i][j] = theMapOfEveryStep[num][i][j];
                    }
                }
                player = (player != true) ? true : false;
                continue;
            }

            if (order.equals("restart")) {
                num = 0;
                numMax = 0;
                System.out.println();
                printMap(mapData, theMapOfEveryStep[num]);
                System.out.println("游戏已重新开始");
                for (int i = 0;i < 7;i++){
                    for (int j = 0;j < 9;j++){
                         animalData[i][j] = theMapOfEveryStep[0][i][j];
                    }
                }
                player = true;
                continue;
            }

            if (order.equals("exit")) {
                System.out.println("you have exited the game");
                break;
            }

            if (order.equals("redo")) {
                if (num == numMax) {
                    System.out.println("已撤销全部悔棋，不能再撤销悔棋了");
                    continue;
                }
                num++;
                printMap(mapData, theMapOfEveryStep[num]);
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 9; j++) {
                        animalData[i][j] = theMapOfEveryStep[num][i][j];
                    }
                }
                player = (player != true) ? true : false;
                continue;
            }//悔棋后还原步骤

            if (order.equals("help")) {
                System.out.println("指令介绍：\n\n" +
                        "1. 移动指令\n" +
                        "\t移动指令由两个部分组成。\n" +
                        "\t第一个部分是数字1-8，根据战斗力分别对应鼠(1),猫(2),狼(3),狗(4),豹(5),虎(6),狮(7),象(8)\n" +
                        "\t第二个部分是字母wasd中的一个,w对应上方向,a对应左方向,s对应下方向,d对应右方向\n" +
                        "\t比如指令\"1d\"表示鼠向右走,\"4w\"表示狗向左走\n\n" +
                        "2. 游戏指令\n" +
                        "\t输入 restart 重新开始游戏\n" +
                        "\t输入 help 查看帮助\n" +
                        "\t输入 undo 悔棋\n" +
                        "\t输入 redo 取消悔棋\n" +
                        "\t输入 exit 退出游戏");
                continue;
            }

            if (order.length() != 2 || order.charAt(0) < '1'|| order.charAt(0) > '8') {
                System.out.println("请输入正确的指令，么么哒");
                continue;
            }
            String placeOfAnimal = findIt(order, animalData);

            switch (moveAnimals(placeOfAnimal, order, animalData,mapData)) {
                //移动动物的错误提示
                case 1:
                    System.out.println("不能越界");
                    player = !player;
                    continue;
                case 2:
                    System.out.println("飞不过去呢");
                    player = !player;
                    continue;
                case 3 :
                    System.out.println("不能下水");
                    player = !player;
                    continue;
                case 4:
                    System.out.println("小动物已西去");
                    player = !player;
                    continue;
                case 5:
                    System.out.println("请输入正确的指令，么么哒");
                    player = !player;
                    continue;
                case 6:
                    System.out.println("不能进己方的家");
                    player = !player;
                    continue;
                case 7:
                    System.out.println("人类为何不能停止互相伤害呢？");
                    player = !player;
                    continue;
                case 8:
                    System.out.println("生命诚可贵，为何要放弃治疗呢");
                    player = !player;
                    continue;
                case 9:
                    System.out.println("猥琐在水里偷袭大象是不合法的你造吗");
                    player = !player;
                    continue;
            }

            printMap(mapData, animalData);//输出动物和地图。

            num++;
            numMax = num;

            theMapOfEveryStep(num, theMapOfEveryStep, animalData);

            if ((animalData[3][0] - '1' >= 0 && animalData[3][0] - '1' < 8) || winOrNot1(animalData) == 1 ||
                    allTheAnimalsWhetherCanToMove(animalData, player,mapData) == 'a') {
                System.out.println("右边赢了");
                break;
            }
            if ((animalData[3][8] - 'a' >= 0 && animalData[3][8] - 'a' < 8) || winOrNot1(animalData) == 2 ||
                    allTheAnimalsWhetherCanToMove(animalData, player,mapData) == '1') {
                System.out.println("左边赢了");
                break;
            }
        }
    }

    public static int judgeWheatherToMove(char char1, char char2, int i, int j) {
        //This method is served for moveAnimals ,the data from it is the same as its
        if (!player) {
            if (i == 3 && j == 0) {
                return 6;
            }
            if (char2 != '0') {
                if (char2 - 'a' >= 0 && char2 - 'a' < 8) {
                    return 7;
                } else if ((i == 2 && j == 0) || (i == 4 && j == 0) || (i == 3 && j == 1)) {
                    return 1;
                } else if (char1 == 'a' && char2 == '8') {
                    return 1;
                } else if (char1 == 'h' && char2 == '1'){
                    return 8;
                } else if (char1 - 'a' >= char2 - '1') {
                    return 1;
                } else {
                    return 8;
                }
            }
        }else {
            if (i == 3 && j == 8) {
                return 6;
            }
            if (char2 != '0') {
                if (char2 - '1' >= 0 && char2 - '1' < 8) {
                    return 7;
                } else if ((i == 2 && j == 8) || (i == 4 && j == 8) || (i == 3 && j == 7)) {
                    return 1;
                } else if (char1 == '1' && char2 == 'h') {
                    return 1;
                }else if (char1 == '8' && char2 == 'a'){
                    return 8;
                } else if (char1 - '1' >= char2 - 'a') {
                    return 1;
                } else {
                    return 8;
                }
            }
        }
        return 1;
    }

    public static void theMapOfEveryStep(int num1, char[][][] char1, char[][] char2) {
        //to remember every step shape of map.
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                char1[num1][i][j] = char2[i][j];
            }
        }
    }

    public static void printMap(char[][] charMap, char[][] charAnimal) {
        //print map and animals at the same time,but snimals is suprior than map
        System.out.println("--------------------------------------");
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (charAnimal[i][j] == '0') {
                    switch (charMap[i][j]) {
                        case '0':
                            System.out.print(" 　 ");
                            break;
                        case '1':
                            System.out.print(" 水 ");
                            break;
                        case '2':
                            System.out.print(" 陷 ");
                            break;
                        case '3':
                            System.out.print(" 家 ");
                            break;
                        case '4':
                            System.out.print(" 陷 ");
                            break;
                        case '5':
                            System.out.print(" 家 ");
                            break;
                    }
                } else {
                    switch (charAnimal[i][j]) {
                        case '1':
                            System.out.print(" 鼠1");
                            break;
                        case '2':
                            System.out.print(" 猫2");
                            break;
                        case '3':
                            System.out.print(" 狼3");
                            break;
                        case '4':
                            System.out.print(" 狗4");
                            break;
                        case '5':
                            System.out.print(" 豹5");
                            break;
                        case '6':
                            System.out.print(" 虎6");
                            break;
                        case '7':
                            System.out.print(" 狮7");
                            break;
                        case '8':
                            System.out.print(" 象8");
                            break;
                        case 'a':
                            System.out.print("1鼠 ");
                            break;
                        case 'b':
                            System.out.print("2猫 ");
                            break;
                        case 'c':
                            System.out.print("3狼 ");
                            break;
                        case 'd':
                            System.out.print("4狗 ");
                            break;
                        case 'e':
                            System.out.print("5豹 ");
                            break;
                        case 'f':
                            System.out.print("6虎 ");
                            break;
                        case 'g':
                            System.out.print("7狮 ");
                            break;
                        case 'h':
                            System.out.print("8象 ");
                            break;
                    }
                }
            }
            System.out.println();
        }
        System.out.println("--------------------------------------");
    }

    static boolean player = true;//TODO :to judge who should move

    public static String findIt(String str, char[][] charAnimal) {
        //find the position of the animal which the user wants to move
        char num = str.charAt(0);
        if (player) {
            num = (char) (num - '1' + 'a');
        }
        String placeOfAnimal = " ";
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (charAnimal[i][j] == num) {
                    placeOfAnimal = "" + i + j;
                    if (!placeOfAnimal.equals(" "))
                        player = (player != true) ? true : false;
                    return placeOfAnimal;
                }
            }
        }
        return placeOfAnimal;
    }

    public static int winOrNot1(char[][] charwin) {
        //judge whether one of them doesn't have animals
        if (player) {//left
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 9; j++) {
                    if (charwin[i][j] - 'a' >= 0 && charwin[i][j] - 'a' <= 7) {
                        return 0;
                    }
                }
            }
            return 1;
        } else {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 9; j++) {
                    if (charwin[i][j] - '1' >= 0 && charwin[i][j] - '1' <= 7) {
                        return 0;
                    }
                }
            }
            return 2;
        }
    }

    public static int allTheAnimalsWhetherCanToMove(char[][] animalData, boolean player,char[][] mapData) {
        //judge whether none of it can move
        char[][] copyAnimalDate = new char[7][9];
        char oneAnimal = '1';
        if (player) {
            oneAnimal = 'a';
        }
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                copyAnimalDate[i][j] = animalData[i][j];
            }
        }
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (copyAnimalDate[i][j] >= oneAnimal && copyAnimalDate[i][j] <= oneAnimal + 7) {
                    if (moveAnimals("" + i + j, " w", copyAnimalDate,mapData) == 0 || moveAnimals("" + i + j, " s", copyAnimalDate,mapData) == 0 ||
                            moveAnimals("" + i + j, " a", copyAnimalDate,mapData) == 0 || moveAnimals("" + i + j, " d", copyAnimalDate,mapData) == 0) {
                        return 0;//return 0  is can move
                    }
                }
            }
        }
        return oneAnimal;//when return ,the left is 'a'，and the right is '1'
    }

    public static int moveAnimals(String placeOfAnimal, String order, char[][] animalData,char[][] mapData) {
        //这个方法用来判断动物能否按照指令移动，如果不能按照指令移动，就输出相应的提示
        //返回值及其意义   1：越界  2：狮虎不能跳河  3：其他动物不能下水  4：动物已经死亡  5：指令错误  6：进自己家  7：吃己方动物 8：送死  9：老鼠从水中攻击大象
        if (placeOfAnimal.equals(" ")) {
            char order1 = order.charAt(0);
            if ((order1 >= '1' && order1 <= '8') || (order1 >= 'a' && order1 <= 'h')) {
                return 4;
            }
            return 5;
        } else {
            int a = placeOfAnimal.charAt(0) - '0';
            int b = placeOfAnimal.charAt(1) - '0';
            char a1,a_1,f6,g7,h_8;//因为后面老鼠需要左右两边，所以定义两个关于老鼠的变量。之所以这样做是将两个步骤统一起来可以起到精简代码的作用

            if (player) {
                a_1 = 'a';
                a1 = '1';
                f6 = '6';
                g7 = '7';
                h_8 = 'h';
            } else {
                a_1 = '1';
                a1 = 'a';
                f6 = 'f';
                g7 = 'g';
                h_8 = '8';
            }//这一部分用一个变量来代替左右两边，可以有效精简代码，不用再分左右两边进行特殊动物的讨论了
            switch (order.charAt(1)) {
                case 'w':
                    if (a - 1 < 0) {
                        return 1;
                    }
                    if ((a == 3 || a == 6) && (b >= 3 && b <= 5)) {
                        if (animalData[a][b] == f6 || animalData[a][b] == g7) {
                            if (animalData[a - 1][b] == a_1 || animalData[a - 2][b] == a_1) {
                                return 2;//判断老虎狮子是否能飞过河
                            } else {
                                if (judgeWheatherToMove(animalData[a][b], animalData[a - 3][b], a - 3, b) == 1) {
                                    animalData[a - 3][b] = animalData[a][b];
                                    animalData[a][b] = '0';
                                } else
                                    return judgeWheatherToMove(animalData[a][b], animalData[a - 3][b], a - 3, b);
                                break;
                            }
                        } else if (animalData[a][b] != a1) {
                            return 3;
                        }
                    }if (mapData[a][b] == '1' && animalData[a - 1][b] == h_8) return  9;
                    if (judgeWheatherToMove(animalData[a][b], animalData[a - 1][b], a - 1, b) == 1) {
                        animalData[a - 1][b] = animalData[a][b];
                        animalData[a][b] = '0';
                    } else return judgeWheatherToMove(animalData[a][b], animalData[a - 1][b], a - 1, b);//如果没有特殊情况就直接判断动物之间的互相伤害返回相应值会得到相应的口令
                    break;

                case 's':
                    if (a + 1 > 6) {
                        return 1;
                    }
                    if ((a == 3 || a == 0) && (b >= 3 && b <= 5)) {
                        if (animalData[a][b] == f6 || animalData[a][b] == g7) {
                            if (animalData[a + 1][b] == a_1 || animalData[a + 2][b] == a_1) {
                                return 2;
                            } else {
                                if (judgeWheatherToMove(animalData[a][b], animalData[a + 3][b], a + 3, b) == 1) {
                                    animalData[a + 3][b] = animalData[a][b];
                                    animalData[a][b] = '0';
                                } else
                                    return judgeWheatherToMove(animalData[a][b], animalData[a + 3][b], a + 3, b);
                                break;
                            }
                        } else if (animalData[a][b] != a1) {
                            return 3;
                        }
                    }if (mapData[a][b] == '1' && animalData[a + 1][b] == h_8) {
                    return 9;
                }
                    if (judgeWheatherToMove(animalData[a][b], animalData[a + 1][b], a + 1, b) == 1) {
                        animalData[a + 1][b] = animalData[a][b];
                        animalData[a][b] = '0';
                    } else return judgeWheatherToMove(animalData[a][b], animalData[a + 1][b], a + 1, b);
                    break;

                case 'a':
                    if (b - 1 < 0) {
                        return 1;
                    }
                    if ((b == 6) && (a == 1 || a == 2 || a == 4 || a == 5)) {
                        if (animalData[a][b] == f6 || animalData[a][b] == g7) {
                            if (animalData[a][b - 1] == a_1 || animalData[a][b - 2] == a_1 || animalData[a][b - 3] == a_1) {
                                return 2;
                            } else {
                                if (judgeWheatherToMove(animalData[a][b], animalData[a][b - 4], a, b - 4) == 1) {
                                    animalData[a][b - 4] = animalData[a][b];
                                    animalData[a][b] = '0';
                                } else
                                    return judgeWheatherToMove(animalData[a][b], animalData[a][b - 4], a, b - 4);
                                break;
                            }
                        } else if (animalData[a][b] != a1) {
                            return 3;
                        }
                    }if (mapData[a][b] == '1' && animalData[a][b - 1] == h_8) return  9;
                    if (judgeWheatherToMove(animalData[a][b], animalData[a][b - 1], a, b - 1) == 1) {
                        animalData[a][b - 1] = animalData[a][b];
                        animalData[a][b] = '0';
                    } else return judgeWheatherToMove(animalData[a][b], animalData[a][b - 1], a, b - 1);
                    break;

                case 'd':
                    if (b + 1 > 8) {
                        return 1;
                    }
                    if ((b == 2) && (a == 1 || a == 2 || a == 4 || a == 5)) {
                        if (animalData[a][b] == f6 || animalData[a][b] == g7) {
                            if (animalData[a][b + 1] == a_1 || animalData[a][b + 2] == a_1 || animalData[a][b + 3] == a_1) {
                                return 2;
                            } else {
                                if (judgeWheatherToMove(animalData[a][b], animalData[a][b + 4], a, b + 4) == 1) {
                                    animalData[a][b + 4] = animalData[a][b];
                                    animalData[a][b] = '0';
                                } else
                                    return judgeWheatherToMove(animalData[a][b], animalData[a][b + 4], a, b + 4);
                                break;
                            }
                        } else if (animalData[a][b] != a1) {
                            return 3;
                        }
                    }if (mapData[a][b] == '1' && animalData[a][b + 1] == h_8) return  9;
                    if (judgeWheatherToMove(animalData[a][b], animalData[a][b + 1], a, b + 1) == 1) {
                        animalData[a][b + 1] = animalData[a][b];
                        animalData[a][b] = '0';
                    } else return judgeWheatherToMove(animalData[a][b], animalData[a][b + 1], a, b + 1);
                    break;

                default:
                    return 5;
            }
        }
        return 0;
    }
}