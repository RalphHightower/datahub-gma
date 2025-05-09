package com.linkedin.metadata.dao.producer;

import com.linkedin.common.AuditStamp;
import com.linkedin.common.urn.Urn;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.data.template.UnionTemplate;
import com.linkedin.metadata.dao.utils.ModelUtils;
import com.linkedin.metadata.events.ChangeType;
import com.linkedin.metadata.events.IngestionMode;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * A base class for all metadata event producers.
 *
 *<p>See http://go/gma for more details.
 */
public abstract class BaseMetadataEventProducer<SNAPSHOT extends RecordTemplate, ASPECT_UNION extends UnionTemplate, URN extends Urn> {

  protected final Class<SNAPSHOT> _snapshotClass;
  protected final Class<ASPECT_UNION> _aspectUnionClass;

  public BaseMetadataEventProducer(@Nonnull Class<SNAPSHOT> snapshotClass,
      @Nonnull Class<ASPECT_UNION> aspectUnionClass) {
    ModelUtils.validateSnapshotAspect(snapshotClass, aspectUnionClass);
    _snapshotClass = snapshotClass;
    _aspectUnionClass = aspectUnionClass;
  }

  public Class<ASPECT_UNION> getAspectUnionClass() {
    return _aspectUnionClass;
  }

  /**
   * Produces a Metadata Change Event (MCE) with a snapshot-base metadata change proposal.
   *
   * @param urn {@link Urn} of the entity
   * @param newValue the proposed new value for the metadata
   * @param <ASPECT> must be a supported aspect type in {@code ASPECT_UNION}
   */
  public abstract <ASPECT extends RecordTemplate> void produceSnapshotBasedMetadataChangeEvent(@Nonnull URN urn,
      @Nonnull ASPECT newValue);

  /**
   * Produces a Metadata Audit Event (MAE) after a metadata aspect is updated for an entity.
   *
   * @param urn {@link Urn} of the entity
   * @param oldValue the value prior to the update, or null if there's none.
   * @param newValue the value after the update
   * @param <ASPECT> must be a supported aspect type in {@code ASPECT_UNION}
   */
  public abstract <ASPECT extends RecordTemplate> void produceMetadataAuditEvent(@Nonnull URN urn,
      @Nullable ASPECT oldValue, @Nullable ASPECT newValue);

  /**
   * Produces an aspect specific Metadata Audit Event (MAE) after a metadata aspect is updated for an entity.
   *
   * @param urn {@link Urn} of the entity
   * @param oldValue the value prior to the update, or null if there's none.
   * @param newValue the value after the update
   * @param aspectClass the class of ASPECT
   * @param auditStamp {@link AuditStamp} containing version auditing information for the metadata change
   * @param ingestionMode {@link IngestionMode} of the change
   */
  public abstract <ASPECT extends RecordTemplate> void produceAspectSpecificMetadataAuditEvent(@Nonnull URN urn,
      @Nullable ASPECT oldValue, @Nullable ASPECT newValue, @Nonnull Class<ASPECT> aspectClass,
      @Nullable AuditStamp auditStamp, @Nullable IngestionMode ingestionMode);

  /**
   * Produces an aspect specific Metadata Audit Event (MAE) after a metadata aspect is updated for an entity with Change type.
   * @param urn {@link Urn} of the entity
   * @param oldValue the value prior to the update, or null if there's none.
   * @param newValue the value after the update
   * @param aspectClass the class of ASPECT
   * @param auditStamp {@link AuditStamp} containing version auditing information for the metadata change
   * @param ingestionMode {@link IngestionMode} of the change
   * @param changeType {@link ChangeType} of the change
   * @param <ASPECT> must be a supported aspect type in {@code ASPECT_UNION}
   */
  public abstract <ASPECT extends RecordTemplate> void produceAspectSpecificMetadataAuditEvent(@Nonnull URN urn,
      @Nullable ASPECT oldValue, @Nullable ASPECT newValue, @Nonnull Class<ASPECT> aspectClass,
      @Nullable AuditStamp auditStamp, @Nullable IngestionMode ingestionMode, ChangeType changeType);

  /**
   * Produce Metadata Graph search metrics inside SearchDAO.
   * TODO: (jejia) Clean this up after we fully migrate to Hosted Search.
   */
  public abstract void produceMetadataGraphSearchMetric(@Nonnull String input, @Nonnull String request,
      @Nonnull String index, @Nonnull List<String> topHits, @Nonnull String api);

}
