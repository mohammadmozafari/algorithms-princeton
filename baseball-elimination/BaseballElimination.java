import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseballElimination
{
    private boolean eliminated = false;
    private int currentTeam = -1, networkSize;
    private int coeType1;
    private int[][] teamsInfo;
    private FordFulkerson ff;
    private HashMap<String, Integer> teamsId;
    private String[] teamsName;

    public BaseballElimination(String filename)
    {
        if (filename == null)
            throw new IllegalArgumentException();

        //define variables
        int n;
        String teamName;
        In in = new In(filename);

        n = in.readInt();
        teamsId = new HashMap<>();
        teamsName = new String[n];
        teamsInfo = new int[n][3 + n];

        for (int i = 0 ; i < n ; i++)
        {
            teamName = in.readString();
            for (int j = 0 ; j < 3 + n ; j++)
            {
                teamsInfo[i][j] = in.readInt();
            }

            teamsId.put(teamName, i);
            teamsName[i] = teamName;
        }

        networkSize = 2 + (n-1) + ((n-1)*(n-2)/2);
    }

    public int numberOfTeams()
    {
        return teamsInfo.length;
    }

    public Iterable<String> teams()
    {
        return teamsId.keySet();
    }

    public int wins(String team)
    {
        if (team == null || !teamsId.containsKey(team))
            throw new IllegalArgumentException();

        int id = teamsId.get(team);

        return teamsInfo[id][0];
    }

    public int losses(String team)
    {
        if (team == null || !teamsId.containsKey(team))
            throw new IllegalArgumentException();

        int id = teamsId.get(team);

        return teamsInfo[id][1];
    }

    public int remaining(String team)
    {
        if (team == null || !teamsId.containsKey(team))
            throw new IllegalArgumentException();

        int id = teamsId.get(team);

        return teamsInfo[id][2];
    }

    public int against(String team1, String team2)
    {
        if (team1 == null || team2 == null || !teamsId.containsKey(team1) || !teamsId.containsKey(team2))
            throw new IllegalArgumentException();

        int id1 = teamsId.get(team1);
        int id2 = teamsId.get(team2);

        return teamsInfo[id1][3 + id2];
    }

    public boolean isEliminated(String team)
    {
        if (team == null || !teamsId.containsKey(team))
            throw new IllegalArgumentException();

        int desiredFlow = 0, gameVertex, teamI, teamJ, cap;
        int thisTeam = teamsId.get(team);

        currentTeam = thisTeam;
        FlowNetwork network = new FlowNetwork(networkSize);
        FlowEdge edge;

        for (int i = 0 ; i < numberOfTeams() ; i++)
        {
            for (int j = i + 1 ; j < numberOfTeams() ; j++)
            {
                if (i != thisTeam && j != thisTeam)
                {
                    gameVertex = getGameVertexNumber(i, j, thisTeam);
                    teamI = getTeamVertexNumber(i, thisTeam);
                    teamJ = getTeamVertexNumber(j, thisTeam);

                    desiredFlow += teamsInfo[i][3 + j];

                    edge = new FlowEdge(0, gameVertex, teamsInfo[i][3 + j]);
                    network.addEdge(edge);

                    edge = new FlowEdge(gameVertex, teamI, Double.POSITIVE_INFINITY);
                    network.addEdge(edge);

                    edge = new FlowEdge(gameVertex, teamJ, Double.POSITIVE_INFINITY);
                    network.addEdge(edge);
                }
            }
        }
        for (int i = 0 ; i < numberOfTeams() ; i++)
        {
            if (i != thisTeam)
            {
                teamI = getTeamVertexNumber(i, thisTeam);
                cap = teamsInfo[thisTeam][0] + teamsInfo[thisTeam][2] - teamsInfo[i][0];

                if (cap < 0)
                {
                    eliminated = true;
                    coeType1 = i;
                    ff =  null;
                    return eliminated;
                }

                edge = new FlowEdge(teamI, networkSize - 1, cap);
                network.addEdge(edge);
            }
        }

        ff = new FordFulkerson(network,0, networkSize - 1);
        eliminated = ff.value() < desiredFlow;
        return eliminated;
    }

    public Iterable<String> certificateOfElimination(String team)
    {
        if (team == null || !teamsId.containsKey(team))
            throw new IllegalArgumentException();

        int thisTeam = teamsId.get(team);

        if (thisTeam != currentTeam)
            isEliminated(team);

        if (!eliminated)
            return null;

        ArrayList<String> coe = new ArrayList<>();
        if (ff == null)
        {
            coe.add(teamsName[coeType1]);
            return coe;
        }

        int k;
        for (int i = networkSize - numberOfTeams() ; i < networkSize - 1; i++)
        {
            k = i - networkSize + numberOfTeams();
            if (ff.inCut(i))
            {
                if (k >= thisTeam)
                    coe.add(teamsName[k + 1]);
                else
                    coe.add(teamsName[k]);
            }
        }
        return coe;
    }

    private int getGameVertexNumber(int id1, int id2, int currentId)
    {
        if (id1 > currentId) id1--;
        if (id2 > currentId) id2--;

        int temp, thisRow, thisRowRemaining;
        thisRow = numberOfTeams() - id1 - 2;
        thisRowRemaining = numberOfTeams() - id2 - 2;

        temp = thisRow * (thisRow - 1) / 2;
        temp += thisRowRemaining;

        return (((numberOfTeams() - 1) * (numberOfTeams() - 2) / 2) - temp);
    }

    private int getTeamVertexNumber(int id, int currentTeam)
    {
        if (id > currentTeam) id--;

        return (networkSize - numberOfTeams() + id);
    }

    public static void main(String[] args)
    {
        BaseballElimination be = new BaseballElimination("teams5.txt");

        System.out.println(be.isEliminated("Detroit"));
    }
}
