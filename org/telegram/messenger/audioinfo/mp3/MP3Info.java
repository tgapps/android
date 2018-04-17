package org.telegram.messenger.audioinfo.mp3;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.messenger.audioinfo.mp3.MP3Frame.Header;

public class MP3Info extends AudioInfo {
    static final Logger LOGGER = Logger.getLogger(MP3Info.class.getName());

    interface StopReadCondition {
        boolean stopRead(MP3Input mP3Input) throws IOException;
    }

    public MP3Info(InputStream input, long fileLength) throws IOException, ID3v2Exception, MP3Exception {
        this(input, fileLength, Level.FINEST);
    }

    public MP3Info(InputStream input, final long fileLength, Level debugLevel) throws IOException, ID3v2Exception, MP3Exception {
        this.brand = "MP3";
        this.version = "0";
        MP3Input data = new MP3Input(input);
        if (ID3v2Info.isID3v2StartPosition(data)) {
            ID3v2Info info = new ID3v2Info(data, debugLevel);
            this.album = info.getAlbum();
            this.albumArtist = info.getAlbumArtist();
            this.artist = info.getArtist();
            this.comment = info.getComment();
            this.cover = info.getCover();
            this.smallCover = info.getSmallCover();
            this.compilation = info.isCompilation();
            this.composer = info.getComposer();
            this.copyright = info.getCopyright();
            this.disc = info.getDisc();
            this.discs = info.getDiscs();
            this.duration = info.getDuration();
            this.genre = info.getGenre();
            this.grouping = info.getGrouping();
            this.lyrics = info.getLyrics();
            this.title = info.getTitle();
            this.track = info.getTrack();
            this.tracks = info.getTracks();
            this.year = info.getYear();
        }
        if (this.duration <= 0 || this.duration >= 3600000) {
            try {
                this.duration = calculateDuration(data, fileLength, new StopReadCondition() {
                    final long stopPosition = (fileLength - 128);

                    public boolean stopRead(MP3Input data) throws IOException {
                        return data.getPosition() == this.stopPosition && ID3v1Info.isID3v1StartPosition(data);
                    }
                });
            } catch (MP3Exception e) {
                if (LOGGER.isLoggable(debugLevel)) {
                    LOGGER.log(debugLevel, "Could not determine MP3 duration", e);
                }
            }
        }
        if ((this.title == null || this.album == null || this.artist == null) && data.getPosition() <= fileLength - 128) {
            data.skipFully((fileLength - 128) - data.getPosition());
            if (ID3v1Info.isID3v1StartPosition(input)) {
                ID3v1Info info2 = new ID3v1Info(input);
                if (this.album == null) {
                    this.album = info2.getAlbum();
                }
                if (this.artist == null) {
                    this.artist = info2.getArtist();
                }
                if (this.comment == null) {
                    this.comment = info2.getComment();
                }
                if (this.genre == null) {
                    this.genre = info2.getGenre();
                }
                if (this.title == null) {
                    this.title = info2.getTitle();
                }
                if (this.track == (short) 0) {
                    this.track = info2.getTrack();
                }
                if (this.year == (short) 0) {
                    this.year = info2.getYear();
                }
            }
        }
    }

    MP3Frame readFirstFrame(MP3Input data, StopReadCondition stopCondition) throws IOException {
        byte b;
        byte b0;
        MP3Input mP3Input = data;
        StopReadCondition stopReadCondition = stopCondition;
        int b02 = 0;
        byte b2 = (byte) -1;
        byte b1 = stopReadCondition.stopRead(mP3Input) ? (byte) -1 : data.read();
        while (b1 != b2) {
            if (b02 == 255 && (b1 & 224) == 224) {
                mP3Input.mark(2);
                byte b22 = stopReadCondition.stopRead(mP3Input) ? b2 : data.read();
                if (b22 == b2) {
                    break;
                }
                byte b3 = stopReadCondition.stopRead(mP3Input) ? b2 : data.read();
                if (b3 == b2) {
                    break;
                }
                Header header = null;
                try {
                    header = new Header(b1, b22, b3);
                } catch (MP3Exception e) {
                }
                if (header != null) {
                    data.reset();
                    mP3Input.mark(header.getFrameSize() + 2);
                    byte[] frameBytes = new byte[header.getFrameSize()];
                    frameBytes[0] = b2;
                    frameBytes[1] = (byte) b1;
                    try {
                        mP3Input.readFully(frameBytes, 2, frameBytes.length - 2);
                        MP3Frame frame = new MP3Frame(header, frameBytes);
                        if (!frame.isChecksumError()) {
                            byte nextB0 = stopReadCondition.stopRead(mP3Input) ? b2 : data.read();
                            byte nextB1 = stopReadCondition.stopRead(mP3Input) ? b2 : data.read();
                            if (nextB0 != b2) {
                                if (nextB1 != b2) {
                                    if (nextB0 == (byte) -1 && (nextB1 & 254) == (b1 & 254)) {
                                        byte nextB2 = stopReadCondition.stopRead(mP3Input) ? b2 : data.read();
                                        byte nextB3 = stopReadCondition.stopRead(mP3Input) ? b2 : data.read();
                                        if (nextB2 == b2) {
                                            b = nextB2;
                                        } else if (nextB3 == b2) {
                                            b = nextB2;
                                        } else {
                                            try {
                                                if (new Header(nextB1, nextB2, nextB3).isCompatible(header)) {
                                                    data.reset();
                                                    try {
                                                        mP3Input.skipFully((long) (frameBytes.length - 2));
                                                        return frame;
                                                    } catch (MP3Exception e2) {
                                                    }
                                                }
                                            } catch (MP3Exception e3) {
                                                b = nextB2;
                                                data.reset();
                                                b0 = b1;
                                                b1 = stopReadCondition.stopRead(mP3Input) ? data.read() : (byte) -1;
                                                b2 = (byte) -1;
                                            }
                                        }
                                        return frame;
                                    }
                                }
                            }
                            return frame;
                        }
                    } catch (EOFException e4) {
                    }
                }
                data.reset();
            }
            b0 = b1;
            if (stopReadCondition.stopRead(mP3Input)) {
            }
            b1 = stopReadCondition.stopRead(mP3Input) ? data.read() : (byte) -1;
            b2 = (byte) -1;
        }
        return null;
    }

    MP3Frame readNextFrame(MP3Input data, StopReadCondition stopCondition, MP3Frame previousFrame) throws IOException {
        Header previousHeader = previousFrame.getHeader();
        data.mark(4);
        int b0 = stopCondition.stopRead(data) ? -1 : data.read();
        int b1 = stopCondition.stopRead(data) ? -1 : data.read();
        if (b0 != -1) {
            if (b1 != -1) {
                if (b0 == 255 && (b1 & 224) == 224) {
                    int b2 = stopCondition.stopRead(data) ? -1 : data.read();
                    int b3 = stopCondition.stopRead(data) ? -1 : data.read();
                    if (b2 != -1) {
                        if (b3 != -1) {
                            Header nextHeader = null;
                            try {
                                nextHeader = new Header(b1, b2, b3);
                            } catch (MP3Exception e) {
                            }
                            if (nextHeader != null && nextHeader.isCompatible(previousHeader)) {
                                byte[] frameBytes = new byte[nextHeader.getFrameSize()];
                                frameBytes[0] = (byte) b0;
                                frameBytes[1] = (byte) b1;
                                frameBytes[2] = (byte) b2;
                                frameBytes[3] = (byte) b3;
                                try {
                                    data.readFully(frameBytes, 4, frameBytes.length - 4);
                                    return new MP3Frame(nextHeader, frameBytes);
                                } catch (EOFException e2) {
                                    return null;
                                }
                            }
                        }
                    }
                    return null;
                }
                data.reset();
                return null;
            }
        }
        return null;
    }

    long calculateDuration(MP3Input data, long totalLength, StopReadCondition stopCondition) throws IOException, MP3Exception {
        MP3Input mP3Input = data;
        StopReadCondition stopReadCondition = stopCondition;
        MP3Frame frame = readFirstFrame(mP3Input, stopReadCondition);
        if (frame != null) {
            int numberOfFrames = frame.getNumberOfFrames();
            if (numberOfFrames > 0) {
                return frame.getHeader().getTotalDuration((long) (frame.getSize() * numberOfFrames));
            }
            long firstFramePosition = data.getPosition() - ((long) frame.getSize());
            long frameSizeSum = (long) frame.getSize();
            int firstFrameBitrate = frame.getHeader().getBitrate();
            long bitrateSum = (long) firstFrameBitrate;
            boolean vbr = false;
            int cbrThreshold = 10000 / frame.getHeader().getDuration();
            MP3Frame frame2 = frame;
            frame = 1;
            while (true) {
                if (frame != cbrThreshold || vbr || totalLength <= 0) {
                    MP3Info mP3Info;
                    boolean vbr2 = vbr;
                    long bitrateSum2 = bitrateSum;
                    MP3Frame readNextFrame = mP3Info.readNextFrame(mP3Input, stopReadCondition, frame2);
                    frame2 = readNextFrame;
                    if (readNextFrame == null) {
                        return (((1000 * frameSizeSum) * ((long) frame)) * 8) / bitrateSum2;
                    }
                    int bitrate = frame2.getHeader().getBitrate();
                    vbr = bitrate != firstFrameBitrate ? true : vbr2;
                    frame++;
                    bitrateSum = bitrateSum2 + ((long) bitrate);
                    frameSizeSum += (long) frame2.getSize();
                    mP3Info = this;
                    mP3Input = data;
                } else {
                    return frame2.getHeader().getTotalDuration(totalLength - firstFramePosition);
                }
            }
        }
        throw new MP3Exception("No audio frame");
    }
}
