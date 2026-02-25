/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

import static ur_os.InterruptType.SCHEDULER_CPU_TO_RQ;
import static ur_os.InterruptType.SCHEDULER_RQ_TO_CPU;

/**
 *
 * @author prestamour
 */
public class RoundRobin extends Scheduler{

    int q;
    int cont;
    boolean multiqueue;
    
    RoundRobin(OS os){
        super(os);
        q = 5; //OJOO: Nuestro quantum es de 5. Mariana tenlo en cuenta para los casos
        cont=0;
    }
    
    RoundRobin(OS os, int q){
        this(os);
        this.q = q;
    }

    RoundRobin(OS os, int q, boolean multiqueue){
        this(os);
        this.q = q;
        this.multiqueue = multiqueue;//Por si algo, para la otra entrega, constructor para la multicola
    }
    
    //Reiniciar el contador del quantum
    void resetCounter(){
        cont=0;
    }
   
    @Override
    public void getNext(boolean cpuEmpty) {
        //si CPU vacío
        if (cpuEmpty) {
            if (!processes.isEmpty()) {//Si hay procesos en la ready queue
                Process siguiente = processes.removeFirst();//Primer proceso
                os.interrupt(SCHEDULER_RQ_TO_CPU, siguiente);//a la cpu!!
                addContextSwitch();
                resetCounter();
            }
            return;
        }
        // CPU ocupaditaa
        cont++;
        Process actual = os.getProcessInCPU();//Quién está en la cpu??
        if (actual.isFinished()) {
            resetCounter();
            return;
        }
        // Si no ha llegado al quantum, siguee
        if (cont < q) {
            return;
        }
        // Se acabó el quantum
        if (!processes.isEmpty()) {
            Process siguiente = processes.removeFirst();
            os.interrupt(SCHEDULER_CPU_TO_RQ, siguiente);//OJO, interrupción para sacar proceso actual y poner el nuevo proceso
            addContextSwitch();
        }
        resetCounter();
    }
 
    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive in this event

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive in this event
    
}
