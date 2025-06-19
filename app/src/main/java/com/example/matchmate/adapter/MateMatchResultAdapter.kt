package com.example.matchmate.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.matchmate.databinding.SingleViewItemBinding
import com.example.matchmate.modelclass.MatchStatus
import com.example.matchmate.modelclass.MateMatchResult

class MateMatchResultAdapter(private val listener: OnUserActionListener) :
    ListAdapter<MateMatchResult, MateMatchResultAdapter.UserViewHolder>(UserDiffCallback()) {

    class UserViewHolder(itemView: SingleViewItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val userProfile: ImageView = itemView.rvUserProfile
        val userReject: ImageView = itemView.rvUserReject
        val userChecked: ImageView = itemView.rvUserChecked
        val nameTextView: TextView = itemView.rvUserName
        val locationTextView: TextView = itemView.rvUserLocation
        val matchScore: TextView = itemView.rvMatchScore
        val matchStatus: TextView = itemView.rvStatus
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            SingleViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)

        Glide.with(holder.itemView.context)
            .load(user.picture.large)
            .into(holder.userProfile)

        val fullName = "${user.name.first} ${user.name.last}"
        holder.nameTextView.text = fullName

        val location = "${user.location.city}, ${user.location.country}"
        holder.locationTextView.text = location

        holder.matchScore.text = "Match Score: ${user.matchScore?.toString() ?: "0"}%"

        holder.matchStatus.text = when (user.status) {
            MatchStatus.ACCEPTED -> {
                holder.matchStatus.isVisible = true
                holder.userReject.isVisible = false
                holder.userChecked.isVisible = false
                "Matched"
            }

            MatchStatus.DECLINED -> {
                holder.matchStatus.isVisible = true
                holder.userReject.isVisible = false
                holder.userChecked.isVisible = false
                "Rejected"
            }

            MatchStatus.NONE -> "Pending"

            else -> {
                holder.matchStatus.isVisible = false
                holder.userReject.isVisible = true
                holder.userChecked.isVisible = true
                "Pending"
            }
        }

        holder.userReject.setOnClickListener {
            listener.onUserReject(user)
        }

        holder.userChecked.setOnClickListener {
            listener.onUserChecked(user)
        }
    }

    interface OnUserActionListener {
        fun onUserReject(user: MateMatchResult)
        fun onUserChecked(user: MateMatchResult)
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<MateMatchResult>() {
    override fun areItemsTheSame(oldItem: MateMatchResult, newItem: MateMatchResult): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MateMatchResult, newItem: MateMatchResult): Boolean {
        return oldItem == newItem
    }
}
