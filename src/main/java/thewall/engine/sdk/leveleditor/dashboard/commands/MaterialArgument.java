package thewall.engine.sdk.leveleditor.dashboard.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thewall.engine.sdk.leveleditor.dashboard.DashboardSession;
import thewall.engine.sdk.leveleditor.dashboard.SpatialService;
import thewall.engine.sdk.leveleditor.dashboard.args.Argument;
import thewall.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import thewall.engine.twilight.entity.Spatial;
import thewall.engine.twilight.material.Material;
import thewall.engine.twilight.utils.Colour;

public class MaterialArgument extends Argument {
    private final static Logger logger = LogManager.getLogger(MaterialArgument.class);
    private final SpatialService service;

    public MaterialArgument(SpatialService service){
        this.service = service;
    }
    @Override
    public void handle(LineArgument arg, DashboardSession session) {
        String[] args = arg.getArguments();
        int spatialID = 0;
        try {
            spatialID = Integer.parseInt(args[0]);
        }catch (Exception e){
            session.writeLine("Syntax error, internal exception: " + e.getMessage());
            logger.warn("Syntax error for [" + arg.getName() + "], internal exception: "+ e.getMessage());
            return;
        }

        Spatial spatial = service.getSpatial(spatialID);
        if(args[1].equalsIgnoreCase("color")) {
            try {
                int r = Integer.parseInt(args[2]), g = Integer.parseInt(args[3]), b = Integer.parseInt(args[4]);
                Colour colour = new Colour(r, g, b);
                spatial.setMaterial(new Material("Box Mesh").loadColour(colour));
                session.writeLine("Material changed to [" + spatialID + "]", Colour.GREEN);
            }catch (Exception e){
                session.writeLine("Syntax error, internal exception: " + e.getMessage(), Colour.RED);
                logger.warn("Syntax error for [" + arg.getName() + "], internal exception: "+ e.getMessage());
            }
        }else {
            session.writeLine("Syntax error, no material type provided", Colour.RED);
            logger.warn("Syntax error for [" + arg.getName() + "], no material type provided");
        }
    }
}
