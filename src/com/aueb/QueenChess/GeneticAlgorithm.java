package com.aueb.QueenChess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GeneticAlgorithm
{
    private ArrayList<Chromosome> population; // population with chromosomes
    private ArrayList<Integer> occurrences; // list with chromosomes (indices) based on fitness score
    private int N;
    GeneticAlgorithm(int N)
    {
        this.N = N;
        this.population = null;
        this.occurrences = null;
    }

    Chromosome run(int populationSize, double mutationProbability, int maxSteps, int minFitness)
    {
        //We initialize the population
        this.initializePopulation(populationSize);
        Random r = new Random();
        for(int step = 0; step < maxSteps; step++)
        {
            //Initialize the new generated population
            ArrayList<Chromosome> newPopulation = new ArrayList<>();
            for(int i = 0; i < populationSize / 2; i++)
            {
                //We choose two chromosomes from the population
                //Due to how fitnessBounds ArrayList is generated, the probability of
                //selecting a specific chromosome depends on its fitness score
                int xIndex = this.occurrences.get(r.nextInt(this.occurrences.size()));
                Chromosome xParent = this.population.get(xIndex);

                int yIndex = this.occurrences.get(r.nextInt(this.occurrences.size()));
                while(xIndex == yIndex)
                {
                    yIndex = this.occurrences.get(r.nextInt(this.occurrences.size()));
                }

                Chromosome yParent = this.population.get(yIndex);
                //We generate the children of the two chromosomes
                Chromosome[] children = this.reproduce(xParent, yParent);

                //We might then mutate the children
                if(r.nextDouble() < mutationProbability)
                {
                    //TODO check N is my opinion to work i don't think is ok
                    children[0].setN(N);
                    children[0].mutate();
                    children[1].setN(N);
                    children[1].mutate();
                }
                //...and finally add them to the new population
                newPopulation.add(children[0]);
                newPopulation.add(children[1]);
            }
            this.population = new ArrayList<>(newPopulation);
            //We sort the population so the one with the greater fitness is first
            this.population.sort(Collections.reverseOrder());
            //If the chromosome with the best fitness is acceptable we return it
            if(this.population.get(0).getFitness() >= minFitness) return this.population.get(0);
            //We update the occurrences list
            this.updateOccurrences();
        }

        return this.population.get(0);
    }

    //We initialize the population by creating random chromosomes
    void initializePopulation(int populationSize)
    {
        this.population = new ArrayList<>();
        for(int i = 0; i < populationSize; i++)
        {
            this.population.add(new Chromosome(N));
        }
        this.updateOccurrences();
    }

    //Updates the list that contains indexes of the chromosomes in the population ArrayList
    void updateOccurrences()
    {
        this.occurrences = new ArrayList<>();
        for(int i = 0; i < this.population.size(); i++)
        {
            for(int j = 0; j < this.population.get(i).getFitness(); j++)
            {
                //Each chromosome index exists in the list as many times as its fitness score
                //By creating this list this way, and choosing a random index from it,
                //the greater the fitness score of a chromosome, the greater chance it will be chosen.
                this.occurrences.add(i);
            }
        }
    }

    //Reproduces two chromosomes and generates their children
    Chromosome[] reproduce(Chromosome x, Chromosome y)
    {
        Random r = new Random();
        //Randomly choose the intersection point
        int intersectionPoint = r.nextInt(this.N);
        int[] firstChild = new int[this.N];
        int[] secondChild = new int[this.N];

        //The first child has the left side of the x chromosome up to the intersection point...
        //The second child has the left side of the y chromosome up to the intersection point...
        for(int i = 0; i < intersectionPoint; i++)
        {
            firstChild[i] = x.getGenes()[i];
            secondChild[i] = y.getGenes()[i];
        }
        //the right side of the y chromosome after the intersection point (for the first)
        //the right side of the x chromosome after the intersection point (for the second)
        for(int i = intersectionPoint; i < firstChild.length; i++)
        {
            firstChild[i] = y.getGenes()[i];
            secondChild[i] = x.getGenes()[i];


        }
        return new Chromosome[] {new Chromosome(firstChild, this.N), new Chromosome(secondChild, this.N)};
    }
}
