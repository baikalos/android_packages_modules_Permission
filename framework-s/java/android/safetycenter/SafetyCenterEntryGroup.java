/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.safetycenter;

import static android.os.Build.VERSION_CODES.TIRAMISU;

import static java.util.Objects.requireNonNull;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A group of conceptually related Safety Center entries.
 *
 * @hide
 */
@SystemApi
@RequiresApi(TIRAMISU)
public final class SafetyCenterEntryGroup implements Parcelable {

    @NonNull
    private final String mId;
    @NonNull
    private final CharSequence mTitle;
    @Nullable
    private final CharSequence mSummary;
    @SafetyCenterEntry.EntrySeverityLevel
    private final int mSeverityLevel;
    @SafetyCenterEntry.SeverityUnspecifiedIconType
    private final int mSeverityUnspecifiedIconType;
    @NonNull
    private final List<SafetyCenterEntry> mEntries;

    private SafetyCenterEntryGroup(
            @NonNull String id,
            @NonNull CharSequence title,
            @Nullable CharSequence summary,
            @SafetyCenterEntry.EntrySeverityLevel int severityLevel,
            @SafetyCenterEntry.SeverityUnspecifiedIconType int severityUnspecifiedIconType,
            @NonNull List<SafetyCenterEntry> entries) {
        mId = requireNonNull(id);
        mTitle = requireNonNull(title);
        mSummary = summary;
        mSeverityLevel = severityLevel;
        mSeverityUnspecifiedIconType = severityUnspecifiedIconType;
        mEntries = new ArrayList<>(entries);
    }

    /**
     * Returns the encoded string ID which uniquely identifies this entry group within the Safety
     * Center on the device for the current user across all profiles and accounts.
     */
    @NonNull
    public String getId() {
        return mId;
    }

    /** Returns the title of this entry group. */
    @NonNull
    public CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Returns the summary string describing this entry group if present, or {@code null} otherwise.
     */
    @Nullable
    public CharSequence getSummary() {
        return mSummary;
    }

    /** Returns the combined severity level of the entries in this entry group. */
    @SafetyCenterEntry.EntrySeverityLevel
    public int getSeverityLevel() {
        return mSeverityLevel;
    }

    /** Returns the {@link SafetyCenterEntry.SeverityUnspecifiedIconType} for this entry group. */
    @SafetyCenterEntry.SeverityUnspecifiedIconType
    public int getSeverityUnspecifiedIconType() {
        return mSeverityUnspecifiedIconType;
    }

    /** Returns the entries that comprise this entry group. */
    @NonNull
    public List<SafetyCenterEntry> getEntries() {
        return new ArrayList<>(mEntries);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SafetyCenterEntryGroup that = (SafetyCenterEntryGroup) o;
        return mSeverityLevel == that.mSeverityLevel
                && mSeverityUnspecifiedIconType == that.mSeverityUnspecifiedIconType
                && Objects.equals(mId, that.mId)
                && TextUtils.equals(mTitle, that.mTitle)
                && TextUtils.equals(mSummary, that.mSummary)
                && Objects.equals(mEntries, that.mEntries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mTitle, mSummary, mSeverityLevel, mSeverityUnspecifiedIconType,
                mEntries);
    }

    @Override
    public String toString() {
        return "SafetyCenterEntryGroup{"
                + "mId='" + mId + '\''
                + ", mTitle=" + mTitle
                + ", mSummary=" + mSummary
                + ", mSeverityLevel=" + mSeverityLevel
                + ", mSeverityUnspecifiedIconType=" + mSeverityUnspecifiedIconType
                + ", mEntries=" + mEntries
                + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(mId);
        TextUtils.writeToParcel(mTitle, dest, flags);
        TextUtils.writeToParcel(mSummary, dest, flags);
        dest.writeInt(mSeverityLevel);
        dest.writeInt(mSeverityUnspecifiedIconType);
        dest.writeTypedList(mEntries);
    }

    @NonNull
    public static final Creator<SafetyCenterEntryGroup> CREATOR =
            new Creator<SafetyCenterEntryGroup>() {
                @Override
                public SafetyCenterEntryGroup createFromParcel(Parcel in) {
                    return new SafetyCenterEntryGroup.Builder(in.readString())
                            .setTitle(TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in))
                            .setSummary(TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in))
                            .setSeverityLevel(in.readInt())
                            .setSeverityUnspecifiedIconType(in.readInt())
                            .setEntries(in.createTypedArrayList(SafetyCenterEntry.CREATOR))
                            .build();
                }

                @Override
                public SafetyCenterEntryGroup[] newArray(int size) {
                    return new SafetyCenterEntryGroup[size];
                }
            };

    /** Builder class for {@link SafetyCenterEntryGroup} */
    public static final class Builder {
        private String mId;
        private CharSequence mTitle;
        private CharSequence mSummary;
        @SafetyCenterEntry.EntrySeverityLevel
        private int mSeverityLevel = SafetyCenterEntry.ENTRY_SEVERITY_LEVEL_UNKNOWN;
        @SafetyCenterEntry.SeverityUnspecifiedIconType
        private int mSeverityUnspecifiedIconType =
                SafetyCenterEntry.SEVERITY_UNSPECIFIED_ICON_TYPE_NO_ICON;
        private List<SafetyCenterEntry> mEntries;

        /**
         * Creates a {@link Builder} for a {@link SafetyCenterEntryGroup}.
         *
         * @param id An encoded string ID that uniquely identifies this
         *           {@link SafetyCenterEntryGroup} among all other entry groups in a given Safety
         *           Center.
         */
        public Builder(@NonNull String id) {
            mId = requireNonNull(id);
        }

        /**
         * Creates a pre-populated {@link Builder} with the values from the given {@link
         * SafetyCenterEntryGroup}.
         */
        public Builder(@NonNull SafetyCenterEntryGroup safetyCenterEntryGroup) {
            mId = safetyCenterEntryGroup.mId;
            mTitle = safetyCenterEntryGroup.mTitle;
            mSummary = safetyCenterEntryGroup.mSummary;
            mSeverityLevel = safetyCenterEntryGroup.mSeverityLevel;
            mSeverityUnspecifiedIconType = safetyCenterEntryGroup.mSeverityUnspecifiedIconType;
            mEntries = new ArrayList<>(safetyCenterEntryGroup.mEntries);
        }

        /** Sets the ID for this entry group. Required. */
        @NonNull
        public Builder setId(@NonNull String id) {
            mId = requireNonNull(id);
            return this;
        }

        /** Sets the title for this entry group. Required. */
        @NonNull
        public Builder setTitle(@NonNull CharSequence title) {
            mTitle = requireNonNull(title);
            return this;
        }

        /** Sets the optional summary text for this entry group. */
        @NonNull
        public Builder setSummary(@Nullable CharSequence summary) {
            mSummary = summary;
            return this;
        }

        /**
         * Sets the {@link SafetyCenterEntry.EntrySeverityLevel} of this entry group. Defaults to
         * {@link SafetyCenterEntry#ENTRY_SEVERITY_LEVEL_UNKNOWN}.
         */
        @NonNull
        public Builder setSeverityLevel(@SafetyCenterEntry.EntrySeverityLevel int severityLevel) {
            mSeverityLevel = severityLevel;
            return this;
        }

        /**
         * Sets the {@link SafetyCenterEntry.SeverityUnspecifiedIconType} of this entry group.
         * Defaults to {@link SafetyCenterEntry#SEVERITY_UNSPECIFIED_ICON_TYPE_NO_ICON}.
         */
        @NonNull
        public Builder setSeverityUnspecifiedIconType(
                @SafetyCenterEntry.SeverityUnspecifiedIconType int severityUnspecifiedIconType) {
            mSeverityUnspecifiedIconType = severityUnspecifiedIconType;
            return this;
        }

        /**
         * Sets the list of {@link SafetyCenterEntry} contained by this entry group. Defaults to
         * an empty list.
         */
        @NonNull
        public Builder setEntries(@NonNull List<SafetyCenterEntry> entries) {
            mEntries = requireNonNull(entries);
            return this;
        }

        /** Creates the {@link SafetyCenterEntryGroup} defined by this {@link Builder}. */
        @NonNull
        public SafetyCenterEntryGroup build() {
            return new SafetyCenterEntryGroup(
                    mId, mTitle, mSummary, mSeverityLevel, mSeverityUnspecifiedIconType, mEntries);
        }
    }
}
