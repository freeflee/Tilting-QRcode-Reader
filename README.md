# Tilting-QRcode-Reader
This QR code reader can interpret messages hierarchically according to the current rotation angle between the QR code and the smartphone's camera.

This project is developed in Android Studio.The QR code decoding source codes are based on the open-source library ZXing and its derived library ZXing-Android-Embedded.
The codes for the Tilting QReader are in the file folder of haomi.TiltingQRcodeReader
The rotation angle is calculated by Finder Patterns of a QR code. The ordinates of Finder Patterns are got from the method of getResultPoints().
The detail calculation for rotation angle can be found in RotationAngle.class.
The codes for messages splitting and sorting can be found in MessageHierarchy.class.
The codes for hierarchical interpretation according to the current rotation angle can be found in HierarchyCaptureActivity.class.

The following are introductions for the demo App Tilting QReader.

In Normal Mode, Tilting QReader will return the decoded results directly, and all the messages will be interpreted to user at a time without any hierarchy.
This mode is commonly used in many other QR code readers.

In Hierarchy Mode, Tilting QReader will interpret and display messages hierarchically according to the current rotation angle.
The rotation angle is defined as the included angle between the mid-perpendicular of a upright QR code and the mid-perpendicular of a smartphone's screen.
Take account of users habits and universal usability, we also define the rotation angle is symmetry and the degrees rotated anticlockwise is the same as the degree rotated clockwise.
Therefore, the value of the rotation angle is in the range of [0, 180].

To cooperate with Hierarchy Mode, The messages used to generating QR code should be organized in the following format (vertical bar "|" is used to hierarchize the messages):
"Most important messages | Secondly important messages | ... | Least important messages | Customized upper-boundary of rotation angle"

If you have any question or find any bug, please feel free to contact me. Thank you very much.

Author:Huang Haomi
Email:hhm8756@gmail.com

