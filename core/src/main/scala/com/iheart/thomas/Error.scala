/*
 * Copyright [2018] [iHeartMedia Inc]
 * All rights reserved
 */

package com.iheart.thomas

import java.time.OffsetDateTime

import cats.data.NonEmptyList
import com.iheart.thomas.model.{Abtest, FeatureName, GroupName, GroupSize}
import lihua.Entity

import scala.util.control.NoStackTrace

sealed abstract class Error extends RuntimeException with NoStackTrace with Product with Serializable

object Error {

  case class FailedToPersist(msg: String) extends Error {
    override def getMessage: FeatureName = msg
  }

  case class ValidationErrors(detail: NonEmptyList[ValidationError]) extends Error
  case class NotFound(msg: Option[String] = None) extends Error

  case class DBException(e: Throwable) extends Error
  case class DBLastError(override val getMessage: String) extends Error

  case class CannotToChangePastTest(start: OffsetDateTime) extends Error

  sealed trait ValidationError extends Product with Serializable

  case class InconsistentGroupSizes(sizes: List[GroupSize]) extends ValidationError

  case object InconsistentTimeRange extends ValidationError
  case class ConflictTest(existing: Entity[Abtest]) extends Error
  case class ConflictCreation(feature: FeatureName) extends Error
  case object CannotScheduleTestBeforeNow extends ValidationError
  case object DuplicatedGroupName extends ValidationError
  case object GroupNameTooLong extends ValidationError
  case object InvalidFeatureName extends ValidationError
  case object InvalidAlternativeIdName extends ValidationError
  case class GroupNameDoesNotExist(name: GroupName) extends ValidationError
  case class ContinuationGap(lastEnd: OffsetDateTime, scheduledStart: OffsetDateTime) extends ValidationError
  case class ContinuationBefore(lasStart: OffsetDateTime, scheduledStart: OffsetDateTime) extends ValidationError

  case object EmptyGroups extends ValidationError
}
