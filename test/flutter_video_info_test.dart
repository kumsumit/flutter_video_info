import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_video_info/flutter_video_info.dart';

void main() {
  test('VideoData parses lower-camel metadata fields', () {
    final data = VideoData.fromJson({
      'path': '/tmp/sample.mp4',
      'title': 'Sample',
      'author': 'Author',
      'album': 'Album',
      'artist': 'Artist',
      'genre': 'Genre',
      'mimeType': 'video/mp4',
      'date': '2026-05-12 10:30:00',
      'location': '+12.34+056.78/',
      'frameRate': '29.97',
      'width': '1920',
      'height': '1080',
      'orientation': '90',
      'fileSize': 123456,
      'duration': '5000',
      'bitrate': '8000000',
      'hasAudio': true,
      'frameCount': '150',
      'isFileExist': true,
    });

    expect(data.title, 'Sample');
    expect(data.mimeType, 'video/mp4');
    expect(data.frameRate, 29.97);
    expect(data.fileSize, 123456);
    expect(data.bitrate, 8000000);
    expect(data.hasAudio, isTrue);
    expect(data.frameCount, 150);
    expect(data.isFileExist, isTrue);
  });
}
