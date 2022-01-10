package jte2.engine.sdk.leveleditor.dashboard.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import jte2.engine.sdk.leveleditor.dashboard.ConsoleSession;
import jte2.engine.sdk.leveleditor.dashboard.SpatialService;
import jte2.engine.sdk.leveleditor.dashboard.args.Argument;
import jte2.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import jte2.engine.twilight.assets.AssetManager;
import jte2.engine.twilight.spatials.Spatial;
import jte2.engine.twilight.material.Material;
import jte2.engine.twilight.material.Colour;
import jte2.engine.twilight.texture.Texture;

public class MaterialArgument extends Argument {
    private final static Logger logger = LogManager.getLogger(MaterialArgument.class);
    private final AssetManager assetManager;
    private final SpatialService service;

    public MaterialArgument(SpatialService service, AssetManager assetManager){
        this.service = service;
        this.assetManager = assetManager;
    }
    @Override
    public void handle(LineArgument arg, ConsoleSession session) {
        String[] args = arg.getArguments();
        int spatialID;
        try {
            spatialID = Integer.parseInt(args[0]);
        }catch (Exception e){
            session.writeLine("Syntax error, At first argument was excepted material type. [" + e.getMessage() + "]");
            logger.warn("Syntax error for [" + arg.getName() + "], internal exception: "+ e.getMessage());
            return;
        }

        Spatial spatial = service.getSpatial(spatialID);
        if(args[1].equalsIgnoreCase("color")) {
            if(arg.getArgumentsSize() != 5){
                session.writeLine("Syntax error, Excepted 4 arguments (material_type, red, green, blue)", Colour.RED);
                return;
            }
            try {
                int r = Integer.parseInt(args[2]), g = Integer.parseInt(args[3]), b = Integer.parseInt(args[4]);
                Colour colour = new Colour(r, g, b);
                spatial.setMaterial(new Material("Box Mesh").loadColour(colour));
                session.writeLine("Material changed to [" + spatialID + "]", Colour.GREEN);
            }catch (Exception e){
                session.writeLine("Syntax error, internal exception: " + e.getMessage(), Colour.RED);
                logger.warn("Syntax error for [" + arg.getName() + "], internal exception: "+ e.getMessage());
            }
        }else if(args[1].equalsIgnoreCase("texture")){
            if(arg.getArgumentsSize() != 3){
                session.writeLine("Syntax error, Excepted 2 arguments (material_type, filename)", Colour.RED);
                return;
            }
            Texture texture = assetManager.loadTexture(args[2]);
            Material material = new Material();
            material.setTexture(texture);
            spatial.setMaterial(material);
            session.writeLine("Texture changed.", Colour.GREEN);
        }else {
            session.writeLine("Syntax error, no material type provided", Colour.RED);
            logger.warn("Syntax error for [" + arg.getName() + "], no material type provided");
        }
    }
}
