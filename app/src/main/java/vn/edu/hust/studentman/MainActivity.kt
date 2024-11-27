package vn.edu.hust.studentman

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

  private val students = mutableListOf(
    StudentModel("Nguyễn Văn An", "SV001"),
    StudentModel("Trần Thị Bảo", "SV002"),
    StudentModel("Lê Hoàng Cường", "SV003")
  )

  private lateinit var studentAdapter: StudentAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // Initialize the adapter
    studentAdapter = StudentAdapter(
      students,
      onEdit = { position -> showEditStudentDialog(position) },
      onDelete = { position -> deleteStudent(position) }
    )

    // Set up RecyclerView
    findViewById<RecyclerView>(R.id.recycler_view_students).apply {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    // Add new student button
    findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      showAddStudentDialog()
    }
  }

  // Add new student
  private fun showAddStudentDialog() {
    val dialogView = layoutInflater.inflate(R.layout.dialog_student, null)
    val nameInput = dialogView.findViewById<TextInputEditText>(R.id.input_student_name)
    val idInput = dialogView.findViewById<TextInputEditText>(R.id.input_student_id)

    AlertDialog.Builder(this)
      .setTitle("Thêm Sinh Viên")
      .setView(dialogView)
      .setPositiveButton("Lưu") { _, _ ->
        val name = nameInput.text.toString()
        val id = idInput.text.toString()
        if (name.isNotBlank() && id.isNotBlank()) {
          students.add(StudentModel(name, id))
          studentAdapter.notifyItemInserted(students.size - 1)
          Toast.makeText(this, "Đã thêm sinh viên!", Toast.LENGTH_SHORT).show()
        } else {
          Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
        }
      }
      .setNegativeButton("Hủy", null)
      .show()
  }

  // Edit student
  private fun showEditStudentDialog(position: Int) {
    val student = students[position]
    val dialogView = layoutInflater.inflate(R.layout.dialog_student, null)
    val nameInput = dialogView.findViewById<TextInputEditText>(R.id.input_student_name)
    val idInput = dialogView.findViewById<TextInputEditText>(R.id.input_student_id)

    nameInput.setText(student.studentName)
    idInput.setText(student.studentId)

    AlertDialog.Builder(this)
      .setTitle("Sửa Sinh Viên")
      .setView(dialogView)
      .setPositiveButton("Cập Nhật") { _, _ ->
        val newName = nameInput.text.toString()
        val newId = idInput.text.toString()
        if (newName.isNotBlank() && newId.isNotBlank()) {
          students[position] = StudentModel(newName, newId)
          studentAdapter.notifyItemChanged(position)
          Toast.makeText(this, "Đã cập nhật thông tin!", Toast.LENGTH_SHORT).show()
        } else {
          Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
        }
      }
      .setNegativeButton("Hủy", null)
      .show()
  }

  // Delete student
  private fun deleteStudent(position: Int) {
    val deletedStudent = students[position]
    students.removeAt(position)
    studentAdapter.notifyItemRemoved(position)

    Snackbar.make(
      findViewById(R.id.main),
      "Đã xóa sinh viên ${deletedStudent.studentName}",
      Snackbar.LENGTH_LONG
    ).setAction("Hoàn tác") {
      students.add(position, deletedStudent)
      studentAdapter.notifyItemInserted(position)
    }.show()
  }
}
