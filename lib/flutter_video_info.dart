library flutter_video_info;

import 'dart:async';
import 'dart:convert';
import 'dart:developer' as developer;

import 'package:flutter/services.dart';
import 'package:path/path.dart';

class FlutterVideoInfo {
  static const MethodChannel _channel = const MethodChannel(
    'flutter_video_info',
  );

  Future<VideoData> getVideoInfo(String path) async {
    final jsonStr = await _channel.invokeMethod('getVidInfo', {"path": path});
    final jsonMap = json.decode(jsonStr);
    if (jsonMap["isFileExist"] != true) {
      developer.log(
        'Video file path is wrong or does not exists. Please recheck file path.',
        name: 'FlutterVideoInfo',
      );
    }
    return VideoData.fromJson(jsonMap);
  }
}

class VideoData {
  String? path;

  /// string
  String? title;

  /// string
  String? author;

  /// string
  String? album;

  /// string
  String? artist;

  /// string
  String? genre;

  /// string
  String? mimeType;

  /// string
  String? date;

  /// string
  String? location;

  /// double
  double? frameRate;

  /// int
  int? width;

  /// int
  int? height;

  /// [Android] API level 17, (0,90,180,270)
  /// (0 - LandscapeRight)
  /// (90 - Portrait)
  /// (180 - LandscapeLeft)
  /// (270 - portraitUpsideDown)
  int? orientation;

  /// bytes
  int? fileSize;

  /// whether the file exists at [path]
  bool isFileExist = false;

  /// millisecond
  double? duration;

  /// bits per second
  int? bitrate;

  /// true when the video contains an audio track
  bool? hasAudio;

  /// total number of video frames, when available
  int? frameCount;

  VideoData({
    required this.path,
    this.title,
    this.author,
    this.album,
    this.artist,
    this.genre,
    this.mimeType,
    this.date,
    this.location,
    this.frameRate,
    this.width,
    this.height,
    this.orientation,
    this.fileSize,
    required this.isFileExist,
    this.duration,
    this.bitrate,
    this.hasAudio,
    this.frameCount,
  });

  VideoData.fromJson(Map<String, dynamic> json) {
    path = (json['path']);
    isFileExist = json["isFileExist"] == true;
    title =
        _readString(json["title"]) ??
        (isFileExist ? basename(json['path']).split(".")[0] : null);
    mimeType = _readString(json["mimeType"]);
    date = _readString(json["date"]);
    location = _readString(json["location"]);
    frameRate = _readDouble(json["frameRate"]);
    author = _readString(json['author']);
    album = _readString(json['album']);
    artist = _readString(json['artist']);
    genre = _readString(json['genre']);
    width = _readInt(json['width']);
    height = _readInt(json['height']);
    orientation = _readInt(json["orientation"]);
    fileSize = _readInt(json['fileSize']);
    duration = isFileExist ? _readDouble(json['duration']) : null;
    bitrate = _readInt(json['bitrate']);
    hasAudio = _readBool(json['hasAudio']);
    frameCount = _readInt(json['frameCount']);
  }

  static String? _readString(dynamic value) {
    if (value == null) {
      return null;
    }
    final stringValue = value.toString();
    return stringValue.isEmpty ? null : stringValue;
  }

  static int? _readInt(dynamic value) {
    if (value is int) {
      return value;
    }
    if (value is num) {
      return value.toInt();
    }
    return int.tryParse(value?.toString() ?? '');
  }

  static double? _readDouble(dynamic value) {
    if (value is double) {
      return value;
    }
    if (value is num) {
      return value.toDouble();
    }
    return double.tryParse(value?.toString() ?? '');
  }

  static bool? _readBool(dynamic value) {
    if (value is bool) {
      return value;
    }
    final stringValue = value?.toString().toLowerCase();
    if (stringValue == 'yes' || stringValue == 'true' || stringValue == '1') {
      return true;
    }
    if (stringValue == 'no' || stringValue == 'false' || stringValue == '0') {
      return false;
    }
    return null;
  }
}
