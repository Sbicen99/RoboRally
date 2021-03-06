package dk.dtu.compute.se.pisd.roborally.model;

import org.jetbrains.annotations.NotNull;




class ImpossibleMoveException extends Exception {
    private Board board;
    private Player player;
    private Space space;
    private Heading heading;





    public ImpossibleMoveException(Player player,
                                   Space space,
                                   Heading heading) {
        super("Move impossible");
        this.player = player;
        this.space = space;
        this.heading = heading;
    }
// ...

    public void moveForward(@NotNull Player player) {
        if (player.board == board) {
            Space space = player.getSpace();
            Heading heading = player.getHeading();
            Space target = board.getNeighbour(space, heading);
            if (target != null) {
                try {
                    moveToSpace(player, target, heading);
                } catch (ImpossibleMoveException e) {
// we don't do anything here for now;
// we just catch the exception so that
// we do no pass it on to the caller
// (which would be very bad style).
                } } } }

    private void moveToSpace(
            @NotNull Player player,
            @NotNull Space space,
            @NotNull Heading heading) throws ImpossibleMoveException {
        Player other = space.getPlayer();
        if (other != null){
            Space target = board.getNeighbour(space, heading);
            if (target != null) {
// XXX Note that there might be additional problems
// with infinite recursion here!
                moveToSpace(other, target, heading);
            } else {
                throw new ImpossibleMoveException(player, space, heading);
            }
        }
        player.setSpace(space);
    }
}

