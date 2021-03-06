package org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof;

import cyclops.collections.immutable.VectorX;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.algebra.Group;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.algebra.GroupElement;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.linearalgebra.GeneratorVector;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.linearalgebra.PeddersenBase;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.linearalgebra.VectorBase;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.util.ProofUtils;

/**
 * Created by buenz on 7/1/17.
 */
public class GeneratorParams<T extends GroupElement<T>> implements PublicParameter {
    private final VectorBase<T> vectorBase;
    private final PeddersenBase<T> base;
    private final Group<T> group;

    public GeneratorParams(VectorBase<T> vectorBase, PeddersenBase<T> base, Group<T> group) {
        this.vectorBase = vectorBase;
        this.base = base;
        this.group = group;
    }

    public VectorBase<T> getVectorBase() {
        return vectorBase;
    }

    public PeddersenBase<T> getBase() {
        return base;
    }

    public Group<T> getGroup() {
        return group;
    }

    public static <T extends GroupElement<T>> GeneratorParams<T> generateParams(int size, Group<T> group) {
        VectorX<T> gs = VectorX.range(0, size).map(i -> ProofUtils.paddedHash("G", i)).map(group::mapInto);
        VectorX<T> hs = VectorX.range(0, size).map(i -> ProofUtils.paddedHash("H", i)).map(group::mapInto);
        T g = group.mapInto(ProofUtils.hash("G"));
        T h = group.mapInto(ProofUtils.hash("V"));
        VectorBase<T> vectorBase = new VectorBase<>(new GeneratorVector<>(gs, group), new GeneratorVector<>(hs, group), h);
        PeddersenBase<T> base = new PeddersenBase<>(g, h, group);
        return new GeneratorParams<>(vectorBase, base, group);

    }
}
