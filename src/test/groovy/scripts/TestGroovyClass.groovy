package scripts

import com.artur114.bananalib.math.m3d.vec.IVec3DM
import com.artur114.thaumrota.client.light.LightSource
import com.artur114.thaumrota.client.light.LineLightSource

class TestGroovyClass {
    static final List<LightSource> pointLight = new ArrayList<>()
    static final List<LineLightSource> lineLight = new ArrayList<>()

    static IVec3DM lastPoint = null;
}
